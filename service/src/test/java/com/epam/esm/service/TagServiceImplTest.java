package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Tag service")
class TagServiceImplTest {

  @InjectMocks private TagServiceImpl tagService;

  @Mock private TagRepository tagRepository;
  @Mock private ModelMapper modelMapper;
  @Mock private Tag mockTag;
  @Mock private TagDTO mockTagDTO;

  @BeforeEach
  void setUp() {
    when(tagService.convertToDTO(mockTag)).thenReturn(mockTagDTO);
    when(tagService.convertToEntity(mockTagDTO)).thenReturn(mockTag);
  }

  @Test
  void getAll_queryForAll_expectedListOfAllTags() {
    // given
    when(tagRepository.getAll()).thenReturn(Collections.singletonList(mockTag));
    // when
    List<TagDTO> tagDTOList = tagService.getAll();
    // then
    verify(tagRepository).getAll();
    assertThat(tagDTOList, hasSize(1));
  }

  @Test
  void getById_givenTagId_expectedTagDTO() {
    // given
    when(tagRepository.get(anyLong())).thenReturn(Optional.of(mockTag));
    // when
    TagDTO tagDTO = tagService.getById(1L);
    // then
    verify(tagRepository).get(anyLong());
    assertThat(tagDTO, is(equalTo(mockTagDTO)));
  }

  @Test
  void create_givenTagDTO_expectedPersistedTagId() {
    // given
    when(tagRepository.create(any(Tag.class))).thenReturn(1L);
    // when
    long tagId = tagService.create(mockTagDTO);
    // then
    verify(tagRepository).create(any(Tag.class));
    assertThat(tagId, is(equalTo(1L)));
  }

  @Test
  void delete_givenTagDTOId_shouldInvokeRepositoryDeleteMethod() {
    // given
    long tagDTOId = 1L;
    when(tagRepository.get(tagDTOId)).thenReturn(Optional.of(mockTag));
    when(tagRepository.delete(any(Tag.class))).thenReturn(true);
    // when
    boolean actualResult = tagService.delete(tagDTOId);
    // then
    verify(tagRepository).get(tagDTOId);
    verify(tagRepository).delete(mockTag);
    assertTrue(actualResult);
  }

  @Test
  void foundDuplicate_givenTagName_expectedTrue() {
    // given
    when(tagRepository.contains(mockTag)).thenReturn(true);
    // when
    boolean actualResult = tagService.foundDuplicate(mockTagDTO);
    // then
    assertTrue(actualResult);
  }
}
