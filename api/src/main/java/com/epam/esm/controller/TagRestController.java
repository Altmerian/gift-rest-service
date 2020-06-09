package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tags")
class TagRestController {

    private final TagService tagService;

    @Autowired
    public TagRestController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    public Tag getById(@PathVariable long id) {
        return tagService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object create(@RequestBody Tag tag) throws ResourceConflictException {
        if (tagService.foundDuplicate(tag.getName())) {
            throw new ResourceConflictException("Your data conflicts with existing resources");
        }
        long result = tagService.create(tag);
        return "Tag has been created, id = " + result;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable("id") long id) {
        return tagService.delete(id);
    }
}
