BEGIN;
INSERT INTO certificates(id, name, description, price, duration_in_days)
VALUES (1, 'Adidas', 'gift', 100.00, 90);
INSERT INTO certificates(id, name, description, price, duration_in_days)
VALUES (2, 'Nike', 'sport', 100.00, 90);

INSERT INTO public.tags(id, name) VALUES (1, 'tag1');
INSERT INTO public.tags(id, name) VALUES (2, 'tag2');
INSERT INTO public.tags(id, name) VALUES (3, 'tag3');

INSERT INTO public.certificates_tags(certificate_id, tag_id) VALUES (1, 1);
INSERT INTO public.certificates_tags(certificate_id, tag_id) VALUES (1, 3);
INSERT INTO public.certificates_tags(certificate_id, tag_id) VALUES (2, 1);
INSERT INTO public.certificates_tags(certificate_id, tag_id) VALUES (2, 2);
COMMIT;