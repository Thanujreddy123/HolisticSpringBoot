package com.example.TaskManagement;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private TaskRepository taskRepository;
    @Transactional(readOnly = true)
    public List<Tasks> getAllTasks() {
        return taskRepository.getAllTaskDueDateDesc();
    }
    @Transactional
    public Tasks save(Tasks task) {
        return taskRepository.saveAndFlush(task);
    }
    @Transactional(readOnly = true)
    public boolean existsById(Long id){
        return taskRepository.existsById(id);
    }
    @Transactional(readOnly = true)
    public Optional<Tasks> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
    public List<CountType> getPercentageGroupByType(){
        return taskRepository.getPercentageGroupByType();
    }
}
