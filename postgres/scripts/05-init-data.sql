-- ============================================
-- بيانات أولية في قاعدة reservations
-- ============================================
\c reservations

INSERT INTO hotels (hotel_uid, name, country, city, address, stars, price)
VALUES
    ('049161bb-badd-4fa8-9d90-87c9a82b0668',
     'Ararat Park Hyatt Moscow',
     'Russian Federation',
     'Moscow',
     'Neglinnaya Street, 4',
     5,
     10000);

-- ============================================
-- بيانات أولية في قاعدة balances
-- ============================================
\c balances

INSERT INTO loyalty (username, reservation_count, status, discount)
VALUES ('Test Max', 10, 'SILVER', 7);