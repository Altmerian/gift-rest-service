DROP FUNCTION user_tags_function(bigint);
CREATE FUNCTION
    user_tags_function(user_id_in bigint)
    RETURNS TABLE (id integer, name varchar, tag_cost numeric) AS
$$
SELECT t.id, t.name, SUM(o.cost) AS tag_cost
FROM orders o
         JOIN orders_certificates oc ON o.id = oc.order_id
         JOIN certificates_tags ct ON oc.certificate_id = ct.certificate_id
         JOIN tags t ON t.id = ct.tag_id
WHERE o.user_id = user_id_in AND o.deleted !=true
GROUP BY t.id, t.name
ORDER BY tag_cost DESC;
$$ LANGUAGE sql;