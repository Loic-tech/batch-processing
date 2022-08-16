package com.medicaldust.batch.services;

import com.medicaldust.batch.repository.RangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RangeService {

    private final RangeRepository repository;


}
