SELECT id, name
FROM user_tags
WHERE tag_count = (SELECT MAX(tag_count) FROM user_tags)
