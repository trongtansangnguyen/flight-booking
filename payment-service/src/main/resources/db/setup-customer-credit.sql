-- SQL Script to setup Customer Credit for testing
-- Run this script in paymentdb database (port 5434)

-- Customer 1: Đủ credit để test happy path
-- UUID: 550e8400-e29b-41d4-a716-446655440000
INSERT INTO customer_credits (id, customer_id, credit_limit, current_balance)
VALUES ('11111111-1111-1111-1111-111111111111', '550e8400-e29b-41d4-a716-446655440000', 10000000, 10000000)
ON CONFLICT (customer_id) DO UPDATE 
SET credit_limit = 10000000, current_balance = 10000000;

-- Customer 2: Credit thấp để test payment failure
-- UUID: 550e8400-e29b-41d4-a716-446655440001
INSERT INTO customer_credits (id, customer_id, credit_limit, current_balance)
VALUES ('22222222-2222-2222-2222-222222222222', '550e8400-e29b-41d4-a716-446655440001', 5000000, 5000000)
ON CONFLICT (customer_id) DO UPDATE 
SET credit_limit = 5000000, current_balance = 5000000;

-- Customer 3: Credit rất thấp
-- UUID: 550e8400-e29b-41d4-a716-446655440002
INSERT INTO customer_credits (id, customer_id, credit_limit, current_balance)
VALUES ('33333333-3333-3333-3333-333333333333', '550e8400-e29b-41d4-a716-446655440002', 1000000, 1000000)
ON CONFLICT (customer_id) DO UPDATE 
SET credit_limit = 1000000, current_balance = 1000000;

-- Verify
SELECT customer_id, credit_limit, current_balance 
FROM customer_credits 
ORDER BY customer_id;

