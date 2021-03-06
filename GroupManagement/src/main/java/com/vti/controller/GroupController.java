package com.vti.controller;

import com.vti.dto.GroupDto;
import com.vti.entity.Group;
import com.vti.form.GroupFormForCreating;
import com.vti.form.GroupFormForFilter;
import com.vti.form.GroupFormForUpdating;
import com.vti.service.IGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/groups")
@CrossOrigin("*")
public class GroupController {
    @Autowired
    private IGroupService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> getAllGroups
            (Pageable pageable,
             @RequestParam(required = false) String search,
             GroupFormForFilter filter
            ) {
        //Convert entities--> dtos
        Page<Group> entitiesPage = service.getAllGroups(pageable, search, filter);
        Page<GroupDto> dtoPage = entitiesPage.map(entity -> {
            GroupDto dto = modelMapper.map(entity, GroupDto.class);
            return dto;
        });
        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getGroupByID(@PathVariable(name = "id") short id) {
        Group entity = service.getGroupByID(id);

        GroupDto dto = modelMapper.map(entity, GroupDto.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}/exists")
    public ResponseEntity<?> existsByName(@PathVariable(name = "name") String name) {
        return new ResponseEntity<>(service.isGroupExistsByName(name), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createGroup(@RequestBody GroupFormForCreating form) {
        service.createGroup(form);
        return new ResponseEntity<String>("Create successfully!", HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable(name = "id") short id, @RequestBody GroupFormForUpdating form) {
        service.updateGroup(id, form);
        return new ResponseEntity<String>("Update Successfully!", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable(name = "id") short id) {
        service.deleteGroup(id);
        return new ResponseEntity<String>("Delete Successfully!", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteGroups(@RequestParam(name = "ids") List<Short> ids) {
        service.deleteGroups(ids);
        return new ResponseEntity<String>("Delete Successfully!", HttpStatus.OK);
    }
}
