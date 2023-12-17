package com.example.essentialnews.Data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatalabRepository extends JpaRepository<Datalab_info, Long> {

    List<Datalab_info> findByUserinfo_Id(Long userId);
}
