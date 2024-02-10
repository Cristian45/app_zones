
INSERT INTO backzone.`zone`
(id, name, description, palms_quantity, is_affected, createdat, updatedat)
VALUES(16, 'zona A', 'descripción de zona a', 100, 'S', '2024-02-09 02:58:00', '2024-02-09 02:58:00');
INSERT INTO backzone.`zone`
(id, name, description, palms_quantity, is_affected, createdat, updatedat)
VALUES(17, 'zona b', 'descripción de zona b', 200, 'S', '2024-02-09 02:58:00', '2024-02-09 02:58:00');
INSERT INTO backzone.`zone`
(id, name, description, palms_quantity, is_affected, createdat, updatedat)
VALUES(18, 'zona c', 'descripción de zona c', 400, 'S', '2024-02-09 02:59:00', '2024-02-09 02:59:00');

INSERT INTO backzone.pest
(id, name, description, createdat, updatedat)
VALUES(12, 'Escama roja', 'Plaga presente en cítricos', '2024-02-09 03:00:00', '2024-02-09 03:00:00');
INSERT INTO backzone.pest
(id, name, description, createdat, updatedat)
VALUES(13, 'gualpa', 'Insecto conocido como el picudo', '2024-02-09 03:01:00', '2024-02-09 03:01:00');


INSERT INTO backzone.zone_pest
(id, createdat, updatedat, zone_id_id, pest_id_id)
VALUES(25, '2024-02-09 03:02:00', '2024-02-09 03:02:00', 16, 13);
INSERT INTO backzone.zone_pest
(id, createdat, updatedat, zone_id_id, pest_id_id)
VALUES(26, '2024-02-09 03:02:00', '2024-02-09 03:02:00', 17, 12);
INSERT INTO backzone.zone_pest
(id, createdat, updatedat, zone_id_id, pest_id_id)
VALUES(27, '2024-02-09 03:03:00', '2024-02-09 03:03:00', 18, 12);
INSERT INTO backzone.zone_pest
(id, createdat, updatedat, zone_id_id, pest_id_id)
VALUES(28, '2024-02-09 03:03:00', '2024-02-09 03:03:00', 18, 13);
