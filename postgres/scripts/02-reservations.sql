-- الاتصال بقاعدة بيانات الحجوزات
\c reservations

-- ============================================
-- جدول الفنادق
-- ============================================
CREATE TABLE hotels (
                        id SERIAL PRIMARY KEY,
                        hotel_uid UUID NOT NULL UNIQUE,
                        name VARCHAR(255) NOT NULL,
                        country VARCHAR(80) NOT NULL,
                        city VARCHAR(80) NOT NULL,
                        address VARCHAR(255) NOT NULL,
                        stars INT,
                        price INT NOT NULL
);

-- ============================================
-- جدول الحجوزات
-- ============================================
CREATE TABLE reservation (
                             id SERIAL PRIMARY KEY,
                             reservation_uid UUID NOT NULL UNIQUE,
                             username VARCHAR(80) NOT NULL,
                             payment_uid UUID NOT NULL,
                             hotel_id INT NOT NULL REFERENCES hotels(id),
                             status VARCHAR(20) NOT NULL CHECK (status IN ('PAID', 'CANCELED')),
                             start_date TIMESTAMP WITH TIME ZONE NOT NULL,
                             end_date TIMESTAMP WITH TIME ZONE NOT NULL
);

-- منح الصلاحيات للمستخدم program
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO program;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO program;