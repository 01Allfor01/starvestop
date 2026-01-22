package com.allforone.starvestop.domain.owner.controller;

import com.allforone.starvestop.domain.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerRepository ownerRepository;


}
