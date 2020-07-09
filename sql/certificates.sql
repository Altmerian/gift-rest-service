SELECT c.id, c.name, c.description, c.creation_date, c.modification_date, c.duration_in_days, t.name
FROM certificates c
         LEFT JOIN certificates_tags ct ON c.id = ct.certificate_id
         LEFT JOIN tags t on ct.tag_id = t.id