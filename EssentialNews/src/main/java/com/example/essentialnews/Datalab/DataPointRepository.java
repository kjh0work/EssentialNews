package com.example.essentialnews.Datalab;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataPointRepository extends JpaRepository<DatalabDataPoint, Long> {
}
