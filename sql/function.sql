CREATE OR REPLACE FUNCTION
    certificates_function(tag_name varchar DEFAULT '%',
                          search_for varchar DEFAULT '%',
                          sort_by varchar DEFAULT 'id')
    RETURNS SETOF record
AS
$$
SELECT c.id, c.name, c.description, c.creation_date, c.modification_date, c.duration_in_days
FROM certificates c
         LEFT JOIN certificates_tags ct ON c.id = ct.certificate_id
         LEFT JOIN tags t on ct.tag_id = t.id
WHERE t.name LIKE tag_name
  AND (c.name LIKE search_for OR c.description LIKE search_for)
ORDER BY sort_by;
$$ LANGUAGE SQL;