package com.example.TaskManagement;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
@CrossOrigin("*")
@AllArgsConstructor
public class TaskController {
    private TaskService tservice;


    @GetMapping("/task")
    public List<Tasks> getTask(){
        return tservice.getAllTasks();
    }
    @GetMapping("/task/vData/percentcounttype")
    public List<CountType> getPercentageGroupByType(){
        return tservice.getPercentageGroupByType();
    }
    @GetMapping("/task/{id}")
    public Tasks getById(@PathVariable Long id){
        return tservice.getTaskById(id).orElseThrow(()->new EntityNotFoundException("Requested task not found"));
    }
    @PostMapping("/post")
    public Tasks addTask(@RequestBody Tasks task){
        return tservice.save(task);
    }
    @DeleteMapping("/task/{id}")
    public ResponseEntity<? extends Object> deleteTask( @PathVariable Long id){
        if(tservice.existsById(id)){
            tservice.delete(id);
            HashMap<String,String> message=new HashMap<>();
            message.put("message",id+"task removed");
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        else{
            HashMap<String,String> message=new HashMap<>();
            message.put("message",id+"task not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }
    @PutMapping("/task/{id}")
    public ResponseEntity<? extends Object> puyTask(@RequestBody Tasks task, @PathVariable Long id){
        if(tservice.existsById(id)){
            Tasks t=tservice.getTaskById(id).orElseThrow(()->new EntityNotFoundException("Requested task not found"));
            t.setTitle(task.getTitle());
            t.setDueDate(task.getDueDate());
            t.setType(task.getType());
            t.setDescription(task.getDescription());
            tservice.save(t);
            return ResponseEntity.ok().body(task);
        }
        else{
            HashMap<String,String> message=new HashMap<>();
            message.put("message",id+"task not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }
}
