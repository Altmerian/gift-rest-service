package com.epam.esm.service;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.math.BigDecimal;
import java.util.List;

public interface CertificateService {

	public List<Certificate> getAll();

	public Certificate getById(long id);

	public long create(Certificate certificate);

	public long update(Certificate certificate);

	public boolean delete(long id);

	public boolean foundDuplicate(String name, int durationInDays, BigDecimal price, List<Tag> tags);
}
