CREATE OR REPLACE VIEW user_tags AS
SELECT t.id, t.name, COUNT(t.id) AS tag_count
FROM orders o
         JOIN orders_certificates oc ON o.id = oc.order_id
         JOIN certificates_tags ct ON oc.certificate_id = ct.certificate_id
         JOIN tags t ON t.id = ct.tag_id
WHERE o.user_id = (SELECT user_id
                   FROM (SELECT user_id, SUM(cost)
                         FROM orders
                         GROUP BY user_id
                         ORDER BY 2 DESC
                         LIMIT 1
                        ) orders)
GROUP BY t.id, t.name
ORDER BY tag_count DESC;
