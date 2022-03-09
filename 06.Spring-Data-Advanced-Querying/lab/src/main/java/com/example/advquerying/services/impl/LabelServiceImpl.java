package com.example.advquerying.services.impl;

import com.example.advquerying.repositories.LabelRepository;
import com.example.advquerying.services.LabelService;
import org.springframework.stereotype.Service;

@Service
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }
}
