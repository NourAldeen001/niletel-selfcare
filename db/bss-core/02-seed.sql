BEGIN;

INSERT INTO tariff_plan (tariff_code, tariff_name, rate_per_min_piasters, active_flag) VALUES
  ('PRE01', 'Nile Prepaid', 19, 'Y'),
  ('PRE02','Nile Youth', 14, 'Y'),
  ('OLD99', 'Classic 2015', 25, 'N');

INSERT INTO operator (username, password_hash, full_name, role_code, active_flag) VALUES
  ('operator1', 'a47cf51747f0edaca5eb0a80c9666391150818c9aca3efb61fa90d8e2d5a4f4c', 'Hany Samir', 'OPR', 'Y'),
  ('selfcare-svc', '4ec877f1dde24869fca2e2f6133d58a4b027ced872cad50b01be775ea4ca35d9', 'Self-Care Service', 'SYS', 'Y');

INSERT INTO subscriber (msisdn, full_name, pin_hash, status, tariff_code, balance_piasters, activation_dt) VALUES
  ('01012345678', 'Mona Adel', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'A', 'PRE01', 6750, DATE '2025-03-01'),
  ('01123456789', 'Karim Hassan', 'fe2592b42a727e977f055947385b709cc82b16b9a87f88c6abf3900d65d0cdc3', 'A', 'PRE02', 500, DATE '2025-06-15'),
  ('01234567890', 'Salma Nabil', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'S', 'PRE01', 2500, DATE '2024-01-20'),
  ('01512345678', 'Youssef Fathy', 'edee29f882543b956620b26d0ee0e7e950399b1c4222f5de05e06425b4c995e9', 'A', 'OLD99', 0, DATE '2016-08-12');

INSERT INTO ledger (txn_id, msisdn, txn_ts, txn_type, amount_piasters, balance_after, ref_no, reason) VALUES
  (50201, '01012345678', TIMESTAMP '2026-10-05 10:15:02', 'R', 5000, 5000, '', 'VOUCHER RECHARGE'),
  (50203, '01234567890', TIMESTAMP '2026-10-06 09:30:00', 'R', 2500, 2500, '', 'VOUCHER RECHARGE'),
  (50205, '01123456789', TIMESTAMP '2026-10-06 11:20:00', 'R',  500,  500, '', 'VOUCHER RECHARGE'),
  (50207, '01012345678', TIMESTAMP '2026-10-07 18:40:11', 'R', 2500, 7500, '', 'VOUCHER RECHARGE'),
  (50212, '01012345678', TIMESTAMP '2026-10-08 21:03:30', 'D',  750, 6750, 'SC-20261008-000004', 'BUNDLE Social Day'),
  (50215, '01012345678', TIMESTAMP '2026-10-09 09:05:47', 'D', 4500, 2250, 'SC-20261009-000007', 'BUNDLE Talk 300'),
  (50216, '01012345678', TIMESTAMP '2026-10-09 09:05:48', 'F', 4500, 6750, 'SC-20261009-000007', 'REFUND');

ALTER TABLE ledger ALTER COLUMN txn_id RESTART WITH 50231;

INSERT INTO voucher (code, value_piasters, used_by, used_at, created_by, created_at) VALUES
  ('10000000000001', 1000, NULL, NULL, 'operator1', TIMESTAMP '2026-10-01 08:00:00'),
  ('10000000000002', 2500, NULL, NULL, 'operator1', TIMESTAMP '2026-10-01 08:00:00'),
  ('10000000000003', 5000, NULL, NULL, 'operator1', TIMESTAMP '2026-10-01 08:00:00'),
  ('10000000000004', 10000, NULL, NULL, 'operator1', TIMESTAMP '2026-10-01 08:00:00'),
  ('10000000000005', 5000, NULL, NULL, 'operator1', TIMESTAMP '2026-10-01 08:00:00'),
  ('10000000000006', 2500, '01234567890', TIMESTAMP '2026-10-06 09:30:00', 'operator1', TIMESTAMP '2026-10-01 08:00:00'),
  ('10000000000007', 500, '01123456789', TIMESTAMP '2026-10-06 11:20:00', 'operator1', TIMESTAMP '2026-10-01 08:00:00'),
  ('10000000000008', 5000, '01012345678', TIMESTAMP '2026-10-05 10:15:02', 'operator1', TIMESTAMP '2026-10-01 08:00:00'),
  ('10000000000009', 2500, '01012345678', TIMESTAMP '2026-10-07 18:40:11', 'operator1', TIMESTAMP '2026-10-01 08:00:00');

-- Making ten unused vouchers from 20000000000001 to 20000000000010
INSERT INTO voucher (code, value_piasters, created_by, created_at)
SELECT '2' || LPAD(n::text, 13, '0'), 5000, 'operator1', TIMESTAMP '2026-10-01 08:00:00'
FROM generate_series(1, 10) AS n;

COMMIT;
