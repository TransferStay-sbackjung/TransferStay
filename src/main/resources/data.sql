
INSERT INTO users (user_id, amount, name)
VALUES
    (1, 1000000, 'buyerUser'),
    (2, 2000000, 'SellerUser'),
    (3, 2500000, 'test1');


INSERT INTO assignment_post (id, user_id, title, description, price, is_auction, location_depth1, location_depth2, reservation_platform, check_in_date, check_out_date, reservation_code, reservation_name, reservation_phone, status, created_at, updated_at)
VALUES
    (1, 1, 'Title 1', 'Description 1', 100000, false, 'Seoul', 'Gangnam', 'Platform A', '2024-10-01', '2024-10-05', 'RES001', 'John Doe', '010-1234-5678', 'ACTIVE', NOW(), NOW()),
    (2, 2, 'Title 2', 'Description 2', 200000, true, 'Busan', 'Haeundae', 'Platform B', '2024-10-02', '2024-10-06', 'RES002', 'Jane Smith', '010-2345-6789', 'ACTIVE', NOW(), NOW()),
    (3, 3, 'Title 3', 'Description 3', 150000, false, 'Incheon', 'Bupyeong', 'Platform C', '2024-10-03', '2024-10-07', 'RES003', 'Alice Brown', '010-3456-7890', 'ACTIVE', NOW(), NOW()),
    (4, 4, 'Title 4', 'Description 4', 300000, true, 'Jeju', 'Jeju-si', 'Platform D', '2024-10-04', '2024-10-08', 'RES004', 'Bob Johnson', '010-4567-8901', 'DELETED', NOW(), NOW()),
    (5, 5, 'Title 5', 'Description 5', 250000, false, 'Daegu', 'Suseong', 'Platform E', '2024-10-05', '2024-10-09', 'RES005', 'Charlie Kim', '010-5678-9012', 'DELETED', NOW(), NOW());