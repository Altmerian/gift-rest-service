DROP FUNCTION certificates_function(character varying, character varying);
CREATE FUNCTION
    certificates_function(tag_name varchar DEFAULT '%',
                          search_for varchar DEFAULT '%')
    RETURNS SETOF certificates AS
$$
SELECT DISTINCT c.id,
                c.name,
                c.description,
                c.price,
                c.creation_date,
                c.modification_date,
                c.duration_in_days
FROM certificates c
         LEFT JOIN certificates_tags ct ON c.id = ct.certificate_id
         LEFT JOIN tags t on ct.tag_id = t.id
WHERE t.name ILIKE tag_name
  AND (c.name ILIKE '%' || search_for || '%' OR c.description ILIKE '%' || search_for || '%');
$$ LANGUAGE sql;