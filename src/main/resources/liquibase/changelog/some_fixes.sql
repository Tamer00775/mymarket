ALTER TABLE product_images
    ADD COLUMN image_data_new bigint;

UPDATE product_images
SET image_data_new = ('x' || md5(image_data))::bit(128)::bigint;

ALTER TABLE product_images
    DROP COLUMN image_data;

ALTER TABLE product_images
    RENAME COLUMN image_data_new TO image_data;

