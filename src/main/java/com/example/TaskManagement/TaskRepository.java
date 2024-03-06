package com.example.TaskManagement;

import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Tasks,Long> {

@Query(value="Select * from tasks order by due_date desc",nativeQuery = true)
    public List<Tasks> getAllTaskDueDateDesc();

@Query(value = "Select new com.example.TaskManagement.CountType(COUNT(*)/(Select COUNT(*) from Tasks)*100,type) from Tasks GROUP BY type")
    public List<CountType> getPercentageGroupByType();

}
