-- ============================================
-- إنشاء المستخدم program بكلمة مرور test
-- ============================================
CREATE USER program WITH PASSWORD 'test';
ALTER USER program WITH SUPERUSER;

-- ============================================
-- إنشاء قواعد البيانات المنطقية (واحدة لكل خدمة)
-- ============================================

-- قاعدة بيانات خدمة الحجز (Reservation Service)
CREATE DATABASE reservations OWNER program;

-- قاعدة بيانات خدمة الدفع (Payment Service)
CREATE DATABASE payments OWNER program;

-- قاعدة بيانات خدمة الولاء (Loyalty Service)
CREATE DATABASE balances OWNER program;