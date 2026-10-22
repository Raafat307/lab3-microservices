-- الاتصال بقاعدة بيانات الولاء
\c balances

-- ============================================
-- جدول الولاء
-- ============================================
CREATE TABLE loyalty (
                         id SERIAL PRIMARY KEY,
                         username VARCHAR(80) NOT NULL UNIQUE,
                         reservation_count INT NOT NULL DEFAULT 0,
                         status VARCHAR(80) NOT NULL DEFAULT 'BRONZE'
                             CHECK (status IN ('BRONZE', 'SILVER', 'GOLD')),
                         discount INT NOT NULL DEFAULT 5
);

-- منح الصلاحيات
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO program;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO program;