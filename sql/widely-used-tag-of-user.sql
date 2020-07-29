EXPLAIN ANALYZE
SELECT id, name
FROM user_tags_function(1)
WHERE tag_cost = (SELECT MAX(tag_cost) FROM user_tags_function(1));

