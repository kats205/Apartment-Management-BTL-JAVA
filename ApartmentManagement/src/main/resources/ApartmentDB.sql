CREATE DATABASE Apartment_Remake;
GO
use Apartment_Remake;
use master
drop database Apartment_Remake;
-- sửa không dấu hết
-- căn hộ không có người ở -> trạng thái là null
-- nb

-- Bảng Role (Vai trò)
CREATE TABLE Role (
    role_id INT PRIMARY KEY IDENTITY(1,1),
    role_name VARCHAR(50) NOT NULL,
    description VARCHAR(MAX),
);
-- Bảng User (Người dùng)
CREATE TABLE [User] (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE CHECK (email LIKE '%@%.%'),
    phone_number VARCHAR(20) CHECK (phone_number LIKE '0[0-9]%' AND LEN(phone_number) = 10) UNIQUE,
    role_id INT NOT NULL,
    active BIT DEFAULT 1,
	avatar_filename VARCHAR(255) NULL,
    created_at DATETIME2 DEFAULT GETDATE(), -- dùng để lưu thời điểm tạo bản ghi
    updated_at DATETIME2 DEFAULT GETDATE(), -- dùng để lưu thời điểm khi có sự cập nhật bản ghi
    FOREIGN KEY (role_id) REFERENCES Role(role_id) -- role_id là khóa ngoại trong user để xác định role của 1 user
);

CREATE TABLE Manager(
	manager_id INT PRIMARY KEY,
	office VARCHAR(100) NOT NULL,
	start_date DATE NOT NULL, -- ngày bổ nhiệm
	FOREIGN KEY (manager_id) REFERENCES [User](user_id)
)
-- Bảng Staff (Nhân viên) - Mở rộng từ User
CREATE TABLE Staff (
    staff_id INT PRIMARY KEY,
    department VARCHAR(50) NOT NULL, -- văn phòng làm việc của nhân viên
    position VARCHAR(50) NOT NULL, -- chức vụ của nhân viên
    hire_date DATE NOT NULL, -- ngày nhân viên được tuyển dụng
	manager_id INT, -- người quản lý trực tiếp nhân viên này
	degree VARCHAR(100),
	certificate VARCHAR(100),
    FOREIGN KEY (staff_id) REFERENCES [User](user_id),
	FOREIGN KEY (manager_id) REFERENCES Manager(manager_id)
);


-- Bảng Building (Tòa nhà)
CREATE TABLE Building (
    building_id INT PRIMARY KEY IDENTITY(1,1),
    building_name VARCHAR(100) NOT NULL,
    address VARCHAR(MAX) NOT NULL,
    total_floors INT NOT NULL,
    total_apartments INT NOT NULL,
    completion_date DATE,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

-- Bảng Apartment (Căn hộ)
CREATE TABLE Apartment (
    apartment_id VARCHAR(20) PRIMARY KEY,
    building_id INT NOT NULL,
    floor INT NOT NULL CHECK(floor >= 0), -- tầng = 0 là tầng trệt
    area FLOAT NOT NULL,
    bedrooms INT NOT NULL,
	price_apartment DECIMAL(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('available', 'occupied', 'maintenance', 'reserved')),
    maintenance_fee DECIMAL(10,2) NOT NULL, -- không nên xài float vì dễ gây lỗi làm tròn số
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(), -- tìm hiểu thêm trigger để tự động cập nhật khi có sự thay đổi
    FOREIGN KEY (building_id) REFERENCES Building(building_id)
);

-- Bảng Resident (Cư dân)
CREATE TABLE Resident (
    resident_id INT PRIMARY KEY IDENTITY(1,1),
    apartment_id VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    identity_card VARCHAR(20) NOT NULL UNIQUE CHECK (identity_card NOT LIKE '%[^0-9]%'), -- chỉ cho phép số
    date_of_birth DATE,
    gender BIT default 0, 	-- 0 là nữ , 1 là nam
--    phone_number VARCHAR(20),
--    email VARCHAR(100),
	-- Vì hệ thống phân quyền có resident nên 2 thuuộc tính này sẽ được lấy ra dựa vào user_id
	user_id INT UNIQUE,
    is_primary_resident BIT DEFAULT 0,
    move_in_date DATE NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (apartment_id) REFERENCES Apartment(apartment_id),
	FOREIGN KEY (user_id) REFERENCES [User](user_id)
);

-- Bảng Service (Dịch vụ)
CREATE TABLE Service (
    service_id INT PRIMARY KEY IDENTITY(1,1),
    service_name VARCHAR(100) NOT NULL,
    description TEXT,
    price_service DECIMAL(10,2) NOT NULL, -- Đã sửa tên cột và kiểu dữ liệu
    unit VARCHAR(20) NOT NULL, -- đơn vị thuê dịch vụ (tháng, năm, lượt)
    is_active BIT DEFAULT 1, -- quản lý trạng thái dịch vụ (1: đang được thuê, 0: chưa được thuê)
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE() -- tạo trigger để tự động update
);

-- Bảng ServiceRegistration (Đăng ký dịch vụ)
CREATE TABLE ServiceRegistration (
    registration_id INT PRIMARY KEY IDENTITY(1,1),
    service_id INT NOT NULL,
    resident_id INT NOT NULL,
	apartment_id VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('active', 'cancelled', 'expired')),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
	registered_by INT NOT NULL,
    FOREIGN KEY (service_id) REFERENCES Service(service_id),
    FOREIGN KEY (resident_id) REFERENCES Resident(resident_id),
    FOREIGN KEY (apartment_id) REFERENCES Apartment(apartment_id),
	FOREIGN KEY (registered_by) REFERENCES Resident(resident_id)
);

-- Bảng Bill (Hóa đơn)
CREATE TABLE Bill (
    bill_id INT PRIMARY KEY IDENTITY(1,1),
    apartment_id VARCHAR(20) NOT NULL,
    billing_date DATE NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'paid', 'overdue', 'cancelled')),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
	billed_to INT NOT NULL,
	late_fee DECIMAL(10,2) DEFAULT 0, -- phí trễ hạn của bill tính 10% cho mỗi ngày trễ hạn, nên tạo thêm trigger để tự động câp nhật phí trễ hạn
    FOREIGN KEY (apartment_id) REFERENCES Apartment(apartment_id),
	FOREIGN KEY (billed_to) REFERENCES Resident(resident_id)
);

-- Bảng BillItem (Chi tiết hóa đơn)
CREATE TABLE BillItem (
    item_id INT PRIMARY KEY IDENTITY(1,1),
    bill_id INT NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    description VARCHAR(MAX),
    amount FLOAT NOT NULL,
    quantity FLOAT NOT NULL DEFAULT 1,
    total FLOAT NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (bill_id) REFERENCES Bill(bill_id)
);

-- Bảng Payment (Thanh toán)
CREATE TABLE Payment (
    payment_id INT PRIMARY KEY IDENTITY(1,1),
    bill_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(50) NOT NULL, -- VNPay, Tiền mặt, chuyển khoảng
    transaction_id VARCHAR(100),
    status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'completed', 'failed', 'refunded')),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (bill_id) REFERENCES Bill(bill_id)
);

CREATE TABLE VnpayReturn (
    id INT PRIMARY KEY IDENTITY(1,1),
    payment_id INT NOT NULL UNIQUE,
    vnp_txn_ref VARCHAR(50) NOT NULL,
    vnp_transaction_no VARCHAR(50),
    vnp_bank_tran_no VARCHAR(50),
    vnp_bank_code VARCHAR(20),
    vnp_card_type VARCHAR(20),
    vnp_order_info VARCHAR(500),
    vnp_pay_date VARCHAR(14),
    vnp_response_code VARCHAR(2),
    vnp_transaction_status VARCHAR(10),
    FOREIGN KEY (payment_id) REFERENCES Payment(payment_id)
);

-- Bảng MaintenanceRequest (Yêu cầu bảo trì)
CREATE TABLE MaintenanceRequest (
    request_id INT PRIMARY KEY IDENTITY(1,1),
    apartment_id VARCHAR(20) NOT NULL,
    resident_id INT NOT NULL,
    request_date DATE NOT NULL,
    description VARCHAR(MAX) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'assigned', 'in_progress', 'completed', 'cancelled')),
    priority VARCHAR(20) NOT NULL CHECK (priority IN ('low', 'medium', 'high', 'urgent')),
    assigned_staff_id INT, -- staff đảm nhiệm yêu càu bảo trì
    completion_date DATE,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (apartment_id) REFERENCES Apartment(apartment_id),
    FOREIGN KEY (resident_id) REFERENCES Resident(resident_id),
    FOREIGN KEY (assigned_staff_id) REFERENCES Staff(staff_id)
);


-- Bảng Notification (Thông báo)
CREATE TABLE Notification (
    notification_id INT PRIMARY KEY IDENTITY(1,1),
    title VARCHAR(200) NOT NULL,
    content VARCHAR(100) NOT NULL,
    creation_date DATETIME2 NOT NULL,
    type VARCHAR(50) NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

-- Bảng NotificationRecipient (Người nhận thông báo)
CREATE TABLE NotificationRecipient (
    notification_recipient_id INT PRIMARY KEY IDENTITY(1,1),
    notification_id INT NOT NULL,
    user_id INT NOT NULL,
    is_read BIT DEFAULT 0,
    read_at DATETIME2 DEFAULT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (notification_id) REFERENCES Notification(notification_id),
    FOREIGN KEY (user_id) REFERENCES [User](user_id),
    CONSTRAINT UC_NotificationUser UNIQUE (notification_id, user_id)
);

-- Bảng Report (Báo cáo)
CREATE TABLE Report (
    report_id INT PRIMARY KEY IDENTITY(1,1),
    report_name VARCHAR(50) NOT NULL,
    generation_date DATETIME2 NOT NULL,
    generated_by_user_id INT NOT NULL,
    parameters VARCHAR(100),
    file_path VARCHAR(255) NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (generated_by_user_id) REFERENCES [User](user_id)
);


--INSERT

-- ===== INSERT DU LIEU THUC TIEN CHO HE THONG QUAN LY CHUNG CU =====

-- 1. Them du lieu vao bang Role
INSERT INTO Role (role_name, description) VALUES
('Manager', 'Quan ly toa nha, chiu trach nhiem van hanh va quan ly cac hoat dong trong toa nha'),
('Staff', 'Nhan vien phuc vu, thuc hien cac cong viec ky thuat va ho tro cu dan'),
('Resident', 'Cu dan sinh song tai chung cu, co quyen su dung cac dich vu va tien ich');

-- 2. Thêm dữ liệu vào bảng User
INSERT INTO [User] (username, password, full_name, email, phone_number, role_id, active) VALUES
-- Managers (role_id = 1)
('manager.huong', '$2a$12$j.yCSNv1mHMgAjD4hkuEY.xYxcyZe.Ua4w3xpoWVZd.Jf7memgEba', 'Tran Thi Huong', 'huong.tran@sunrisevn.com', '0901000002', 1, 1), --mk:Manager@2024#
('manager.minh', '$2a$12$j.yCSNv1mHMgAjD4hkuEY.xYxcyZe.Ua4w3xpoWVZd.Jf7memgEba', 'Le Van Minh', 'minh.le@sunrisevn.com', '0901000003', 1, 1),

-- Staff (role_id = 2)

('staff.tung', '$2a$12$0MRetD48MmFtaS8hPIVC1.u59tvp58uHBnqN5J0dgACoSspTBeiim', 'Pham Van Tung', 'tung.pham@sunrisevn.com', '0901000004', 2, 1), --mk: Staff@2024#
('staff.lan', '$2a$12$0MRetD48MmFtaS8hPIVC1.u59tvp58uHBnqN5J0dgACoSspTBeiim', 'Hoang Thi Lan', 'lan.hoang@sunrisevn.com', '0901000005', 2, 1),
('staff.hai', '$2a$12$0MRetD48MmFtaS8hPIVC1.u59tvp58uHBnqN5J0dgACoSspTBeiim', 'Do Van Hai', 'hai.do@sunrisevn.com', '0901000006', 2, 1),
('staff.linh', '$2a$12$0MRetD48MmFtaS8hPIVC1.u59tvp58uHBnqN5J0dgACoSspTBeiim', 'Nguyen Thi Linh', 'linh.nguyen@sunrisevn.com', '0901000007', 2, 1),

-- Residents (role_id = 3)
('resident.an', '$2a$12$zuZojdSAf830ciuhHnAqkeA6uQAnfATJONcm39GJkobjVa8kcroqa', 'Nguyen Van An', 'anvn.personal@gmail.com', '0912345678', 3, 1), --mk:Resident@123
('resident.bich', '$2a$12$zuZojdSAf830ciuhHnAqkeA6uQAnfATJONcm39GJkobjVa8kcroqa', 'Tran Thi Bich', 'bichtran88@yahoo.com', '0923456789', 3, 1),
('resident.cuong', '$2a$12$zuZojdSAf830ciuhHnAqkeA6uQAnfATJONcm39GJkobjVa8kcroqa', 'Le Van Cuong', 'cuongle1985@hotmail.com', '0934567890', 3, 1),
('resident.dung', '$2a$12$zuZojdSAf830ciuhHnAqkeA6uQAnfATJONcm39GJkobjVa8kcroqa', 'Pham Thi Dung', 'dungpham.work@gmail.com', '0945678901', 3, 1),
('resident.em', '$2a$12$zuZojdSAf830ciuhHnAqkeA6uQAnfATJONcm39GJkobjVa8kcroqa', 'Hoang Van Em', 'emhoang.biz@gmail.com', '0956789012', 3, 1),
('resident.hoa', '$2a$12$zuZojdSAf830ciuhHnAqkeA6uQAnfATJONcm39GJkobjVa8kcroqa', 'Dang Thi Hoa', 'hoadang.home@gmail.com', '0967890123', 3, 1),
('resident.giang', '$2a$12$zuZojdSAf830ciuhHnAqkeA6uQAnfATJONcm39GJkobjVa8kcroqa', 'Vu Thi Giang', 'giangvu.family@gmail.com', '0978901234', 3, 1),
('resident.khanh', '$2a$12$zuZojdSAf830ciuhHnAqkeA6uQAnfATJONcm39GJkobjVa8kcroqa', 'Truong Van Khanh', 'khanhtruong@outlook.com', '0989012345', 3, 1),
('resident.mai', '$2a$12$zuZojdSAf830ciuhHnAqkeA6uQAnfATJONcm39GJkobjVa8kcroqa', 'Phan Thi Mai', 'maiphan@gmail.com', '0989016345', 3, 1),
('resident.nam', '$2a$12$zuZojdSAf830ciuhHnAqkeA6uQAnfATJONcm39GJkobjVa8kcroqa', 'Dinh Van Nam', 'namdinh@gmail.com', '0981016345', 3, 1);


-- 3. Them du lieu vao bang Manager
INSERT INTO Manager (manager_id, office, start_date) VALUES
(1, 'Phong Quan ly chung cu S3', '2023-01-15'),
(2, 'Phong Quan ly chung cu S1', '2023-01-15'),
(3, 'Phong Quan ly chung cu S2', '2023-03-20');

-- 4. Them du lieu vao bang Staff
INSERT INTO Staff (staff_id, department, position, hire_date, manager_id, degree, certificate) VALUES
(4, 'Ky thuat dien nuoc', 'Ky thuat vien dien', '2023-02-01', 2, 'Cao dang Dien - Dien tu', 'Chung chi An toan dien ha the, Chung chi Tho dien bac 4'),
(5, 'Dich vu khach hang', 'Nhan vien tiep tan', '2023-02-15', 2, 'Dai hoc Quan tri Kinh doanh', 'Chung chi Tieng Anh B2, Chung chi Tin hoc van phong'),
(6, 'Ky thuat bao tri', 'Tho sua chua da nang', '2023-04-01', 3, 'Trung cap Co khi dong luc', 'Chung chi Han dien, Chung chi Sua chua may moc'),
(3, 'An ninh - Bao ve', 'Nhan vien bao ve', '2023-05-10', 2, 'Trung hoc pho thong', 'Chung chi Bao ve dan pho, Chung chi PCCC');

-- 5. Them du lieu vao bang Building
INSERT INTO Building (building_name, address, total_floors, total_apartments, completion_date) VALUES
('Chung cu S1', '512 Nguyen Xien, Thanh Pho, Thu Duc, Ho Chi Minh', 25, 200, '2022-12-15'),
('Chung cu S2', '512 Nguyen Xien, Thanh Pho, Thu Duc, Ho Chi Minh', 30, 240, '2023-06-30');

-- 6. Them du lieu vao bang Apartment
INSERT INTO Apartment (apartment_id, building_id, floor, area, bedrooms, price_apartment, status, maintenance_fee) VALUES
-- Toa Sunrise Tower (building_id = 1)
('S1P0101', 1, 1, 65.5, 2, 2800000000.00, 'occupied', 850000.00),
('S1P0102', 1, 1, 78.2, 3, 3200000000.00, 'occupied', 950000.00),
('S1P0103', 1, 1, 45.8, 1, 2100000000.00, 'available', 650000.00),
('S1P0201', 1, 2, 65.5, 2, 2850000000.00, 'occupied', 850000.00),
('S1P0202', 1, 2, 78.2, 3, 3250000000.00, 'reserved', 950000.00),
('S1P0301', 1, 3, 89.7, 3, 3800000000.00, 'occupied', 1100000.00),
('S1P0302', 1, 3, 95.3, 4, 4200000000.00, 'occupied', 1200000.00),
('S1P0501', 1, 5, 120.5, 4, 5500000000.00, 'occupied', 1500000.00),
('S1P1001', 1, 10, 78.2, 3, 3400000000.00, 'maintenance', 950000.00),
('S1P1501', 1, 15, 95.3, 4, 4500000000.00, 'available', 1200000.00),

-- Toa Golden Park (building_id = 2)
('S2P0101', 2, 1, 72.8, 2, 3100000000.00, 'occupied', 900000.00),
('S2P0201', 2, 2, 85.4, 3, 3600000000.00, 'occupied', 1050000.00),
('S2P0301', 2, 3, 105.2, 4, 4800000000.00, 'available', 1300000.00);

-- 7. Them du lieu vao bang Resident
INSERT INTO Resident (apartment_id, full_name, identity_card, date_of_birth, gender, user_id, is_primary_resident, move_in_date) VALUES
-- Cư dân Tòa Sunrise Tower
('S1P0101', 'Nguyen Van An', '079088001234', '1988-05-15', 1, 7, 1, '2023-01-20'),
('S1P0101', 'Tran Thi Bich', '079088005678', '1990-08-22', 0, 8, 0, '2023-01-20'),
('S1P0102', 'Le Van Cuong', '079085001122', '1985-12-10', 1, 9, 1, '2023-02-15'),
('S1P0201', 'Pham Thi Dung', '079092003344', '1992-03-18', 0, 10, 1, '2023-03-10'),
('S1P0301', 'Hoang Van Em', '079080005566', '1980-07-25', 1, 11, 1, '2023-04-05'),
('S1P0301', 'Dang Thi Hoa', '079082007788', '1982-11-30', 0, 12, 0, '2023-04-05'),
('S1P0302', 'Vu Thi Giang', '079090009900', '1990-09-12', 0, 13, 1, '2023-05-01'),
('S1P0501', 'Truong Van Khanh', '079075001357', '1975-04-08', 1, 14, 1, '2023-06-15'),
-- Cư dân Tòa Golden Park
('S2P0101', 'Phan Thi Mai', '079087002468', '1987-01-20', 0, 15, 1, '2023-07-01'),
('S2P0201', 'Dinh Van Nam', '079083003579', '1983-06-14', 1, 16, 1, '2023-08-10');

-- 8. Them du lieu vao bang Service
INSERT INTO Service (service_name, description, price_service, unit, is_active) VALUES
('Dich vu Internet cap quang', 'Cung cap Internet toc do cao 100Mbps, bao gom modem WiFi va ho tro ky thuat 24/7', 300000.00, 'thang', 1),
('Dich vu truyen hinh cap', 'Goi truyen hinh co ban voi 150+ kenh trong nuoc va quoc te', 200000.00, 'thang', 1),
('Dich vu giat ui', 'Dich vu giat ui tai nha, nhan va giao do theo lich hen', 15000.00, 'kg', 1),
('Dich vu don dep nha cua', 'Dich vu don dep can ho dinh ky hoac theo yeu cau', 200000.00, 'luot', 1),
('Dich vu gui xe may', 'Cho gui xe may trong tang ham co bao ve 24/7', 150000.00, 'thang', 1),
('Dich vu gui xe o to', 'Cho gui xe o to trong tang ham co camera giam sat', 800000.00, 'thang', 1),
('Dich vu the duc the thao', 'Su dung phong gym, be boi va san tennis trong khu chung cu', 500000.00, 'thang', 1),
('Dich vu bao ve 24/7', 'Dich vu an ninh va bao ve can ho rieng', 1000000.00, 'thang', 0);

-- 9. Them du lieu vao bang ServiceRegistration
INSERT INTO ServiceRegistration (service_id, resident_id, apartment_id, start_date, end_date, status, registered_by) VALUES
-- Resident 1 (Nguyen Van An - S1P0101)
(1, 1, 'S1P0101', '2023-02-01', '2024-01-31', 'active', 1),
(2, 1, 'S1P0101', '2023-02-01', '2024-01-31', 'active', 1),
(5, 1, 'S1P0101', '2023-02-01', '2024-01-31', 'active', 1),
(7, 1, 'S1P0101', '2023-03-01', '2024-02-29', 'active', 1),

-- Resident 3 (Le Van Cuong - S1P0102)
(1, 3, 'S1P0102', '2023-03-01', '2024-02-29', 'active', 3),
(6, 3, 'S1P0102', '2023-03-01', '2024-02-29', 'active', 3),

-- Resident 4 (Pham Thi Dung - S1P0201)
(1, 4, 'S1P0201', '2023-04-01', '2024-03-31', 'active', 4),
(3, 4, 'S1P0201', '2023-04-15', '2023-10-15', 'expired', 4),
(5, 4, 'S1P0201', '2023-04-01', '2024-03-31', 'active', 4),

-- Resident 5 (Hoang Van Em - S1P0301)
(1, 5, 'S1P0301', '2023-05-01', '2024-04-30', 'active', 5),
(2, 5, 'S1P0301', '2023-05-01', '2024-04-30', 'active', 5),
(6, 5, 'S1P0301', '2023-05-01', '2024-04-30', 'active', 5),
(7, 5, 'S1P0301', '2023-05-01', '2024-04-30', 'active', 5);


-- 10. Thêm dữ liệu vào bảng Bill
INSERT INTO Bill (apartment_id, billing_date, total_amount, status, billed_to, late_fee) VALUES
-- Hóa đơn tháng 1/2024
('S1P0101', '2024-01-05', 1850000.00, 'paid', 1, 0.00),
('S1P0102', '2024-01-05', 1750000.00, 'paid', 3, 0.00),
('S1P0201', '2024-01-05', 1350000.00, 'paid', 4, 0.00),
('S1P0301', '2024-01-05', 2900000.00, 'paid', 5, 0.00),
('S1P0302', '2024-01-05', 1200000.00, 'paid', 7, 0.00),
('S1P0501', '2024-01-05', 1500000.00, 'paid', 8, 0.00),

-- Hóa đơn tháng 2/2024
('S1P0101', '2024-02-05', 1950000.00, 'paid', 1, 0.00),
('S1P0102', '2024-02-05', 1750000.00, 'overdue', 3, 175000.00),
('S1P0201', '2024-02-05', 1350000.00, 'paid', 4, 0.00),
('S1P0301', '2024-02-05', 2900000.00, 'paid', 5, 0.00),

-- Hóa đơn tháng 3/2024 (mới nhất)
('S1P0101', '2024-03-05', 1850000.00, 'pending', 1, 0.00),
('S1P0102', '2024-03-05', 1750000.00, 'pending', 3, 0.00),
('S1P0201', '2024-03-05', 1350000.00, 'pending', 4, 0.00),
('S1P0301', '2024-03-05', 2900000.00, 'pending', 5, 0.00);


-- 11. Them du lieu vao bang BillItem
INSERT INTO BillItem (bill_id, item_type, description, amount, quantity, total) VALUES
-- Bill 1 (S1P0101 - Thang 1/2024)
(1, 'Phi quan ly', 'Phi quan ly chung cu thang 1/2024', 850000, 1, 850000),
(1, 'Tien dien', 'Tien dien thang 1/2024 - 180 kWh', 2500, 180, 450000),
(1, 'Tien nuoc', 'Tien nuoc thang 1/2024 - 15 m3', 20000, 15, 300000),
(1, 'Internet', 'Cuoc Internet thang 1/2024', 300000, 1, 300000),

-- Bill 2 (S1P0102 - Thang 1/2024)
(2, 'Phi quan ly', 'Phi quan ly chung cu thang 1/2024', 950000, 1, 950000),
(2, 'Tien dien', 'Tien dien thang 1/2024 - 200 kWh', 2500, 200, 500000),
(2, 'Tien nuoc', 'Tien nuoc thang 1/2024 - 12 m3', 20000, 12, 240000),
(2, 'Gui xe o to', 'Phi gui xe o to thang 1/2024', 800000, 1, 800000),

-- Bill 7 (S1P0101 - Thang 2/2024)
(7, 'Phi quan ly', 'Phi quan ly chung cu thang 2/2024', 850000, 1, 850000),
(7, 'Tien dien', 'Tien dien thang 2/2024 - 220 kWh', 2500, 220, 550000),
(7, 'Tien nuoc', 'Tien nuoc thang 2/2024 - 18 m3', 20000, 18, 360000),
(7, 'Internet', 'Cuoc Internet thang 2/2024', 300000, 1, 300000),

-- Bill 11 (S1P0101 - Thang 3/2024)
(11, 'Phi quan ly', 'Phi quan ly chung cu thang 3/2024', 850000, 1, 850000),
(11, 'Tien dien', 'Tien dien thang 3/2024 - 160 kWh', 2500, 160, 400000),
(11, 'Tien nuoc', 'Tien nuoc thang 3/2024 - 14 m3', 20000, 14, 280000),
(11, 'Internet', 'Cuoc Internet thang 3/2024', 300000, 1, 300000);


-- 12. Them du lieu vao bang Payment
INSERT INTO Payment (bill_id, amount, payment_date, payment_method, transaction_id, status) VALUES
-- thêm một số dữ liệu có trạng thái khác nhau để java lấy biểu đồ tròn và trụ
-- vì đây là dữ liệu mẫu chỉ thay đổi ngày tháng năm và trạng thái nên sẽ bị trùng transaction_id

(1, 1850000.00, '2024-01-10', 'VNPay', 'VNP20240110001', 'completed'),
(2, 1750000.00, '2024-01-12', 'VNPay', 'VNP20240112001', 'completed'),
(3, 1350000.00, '2024-01-08', 'Tien mat', 'CASH20240108001', 'completed'),
(4, 2900000.00, '2024-01-15', 'VNPay', 'VNP20240115001', 'completed'),
(5, 1200000.00, '2024-01-20', 'VNPay', 'VNP20240120001', 'completed'),
(6, 1500000.00, '2024-01-25', 'VNPay', 'VNP20240125001', 'completed'),
(7, 1950000.00, '2024-02-08', 'VNPay', 'VNP20240208001', 'completed'),
(9, 1350000.00, '2024-02-12', 'Tien mat', 'CASH20240212001', 'completed'),
(10, 2900000.00, '2024-02-18', 'VNPay', 'VNP20240218001', 'completed'),

(1, 1850000.00, '2024-01-11', 'VNPay', 'VNP20240110001', 'pending'),
(2, 1750000.00, '2024-01-12', 'VNPay', 'VNP20240112001', 'pending'),
(3, 1350000.00, '2024-01-07', 'Tien mat', 'CASH20240108001', 'pending'),
(4, 2900000.00, '2024-01-16', 'VNPay', 'VNP20240115001', 'pending'),
(5, 1200000.00, '2024-01-21', 'VNPay', 'VNP20240120001', 'pending'),
(6, 1500000.00, '2024-01-25', 'VNPay', 'VNP20240125001', 'pending'),
(7, 1950000.00, '2024-02-09', 'VNPay', 'VNP20240208001', 'pending'),
(9, 1350000.00, '2024-02-12', 'Tien mat', 'CASH20240212001', 'pending'),
(10, 2900000.00, '2024-02-18', 'VNPay', 'VNP20240218001', 'pending'),

(1, 1850000.00, '2024-01-11', 'VNPay', 'VNP20240110009', 'failed'),
(2, 1750000.00, '2024-01-12', 'Tien mat', 'VNP20240112031', 'failed'),
(3, 1350000.00, '2024-01-07', 'Tien mat', 'CASH20240108201', 'failed'),
(4, 2900000.00, '2024-01-17', 'VNPay', 'VNP20240115007', 'failed'),
(5, 1200000.00, '2024-01-22', 'VNPay', 'VNP20240120006', 'failed'),
(6, 1500000.00, '2024-01-26', 'Tien mat', 'VNP20240125005', 'failed'),
(7, 1950000.00, '2024-02-09', 'VNPay', 'VNP20240208004', 'failed'),
(9, 1350000.00, '2024-02-12', 'Tien mat', 'CASH20240212003', 'failed'),
(10, 2900000.00, '2024-02-20', 'VNPay', 'VNP20240218002', 'failed'),

(2, 1550000.00, '2024-01-14', 'VNPay', 'VNP20240110009', 'refunded'),
(1, 1850000.00, '2024-01-13', 'Tien mat', 'VNP20240112031', 'refunded'),
(4, 130000.00, '2024-01-11', 'Tien mat', 'CASH20240108201', 'refunded'),
(7, 2900000.00, '2024-01-20', 'VNPay', 'VNP20240115007', 'refunded'),
(6, 120000.00, '2024-01-23', 'VNPay', 'VNP20240120006', 'refunded'),
(5, 150000.00, '2024-01-21', 'Tien mat', 'VNP20240125005', 'refunded'),
(8, 190000.00, '2024-02-03', 'VNPay', 'VNP20240208004', 'completed'),
(10, 130000.00, '2024-03-23', 'Tien mat', 'CASH20240212003', 'completed'),
(9, 290000.00, '2024-02-21', 'VNPay', 'VNP20240218002', 'completed'),

(8, 1200000.00, '2024-01-20', 'VNPay', 'VNP20240120001', 'pending'),
(8, 1500000.00, '2024-01-25', 'VNPay', 'VNP20240125001', 'pending'),
(2, 1950000.00, '2024-02-08', 'VNPay', 'VNP20240208001', 'pending'),
(2, 1350000.00, '2024-02-12', 'Tien mat', 'CASH20240212001', 'pending');

-- 13. Them du lieu vao bang VnpayReturn (cho cac giao dich VNPay)
INSERT INTO VnpayReturn (payment_id, vnp_txn_ref, vnp_transaction_no, vnp_bank_code, vnp_order_info, vnp_pay_date, vnp_response_code, vnp_transaction_status) VALUES
(1, 'BILL000001', '14157001', 'VCB', 'Thanh toan hoa don S1P0101 thang 01/2024', '20240110123000', '00', '00'),
(2, 'BILL000002', '14158739', 'NCB', 'Thanh toan hoa don S1P0102 thang 01/2024', '20240112103045', '00', '00'),
(4, 'BILL000004', '14158045', 'ACB', 'Thanh toan hoa don S1P0104 thang 01/2024', '20240115110000', '00', '00'),
(5, 'BILL000005', '14159876', 'TCB', 'Thanh toan hoa don S1P0105 thang 01/2024', '20240120143022', '00', '00'),
(6, 'BILL000006', '14159078', 'BIDV', 'Thanh toan hoa don S1P0106 thang 01/2024', '20240125143000', '00', '00'),
(7, 'BILL000007', '14167234', 'VCB', 'Thanh toan hoa don S1P0107 thang 02/2024', '20240208094512', '00', '00');


-- 14. Them du lieu vao bang MaintenanceRequest
INSERT INTO MaintenanceRequest (apartment_id, resident_id, request_date, description, status, priority, assigned_staff_id, completion_date) VALUES
('S1P0101', 1, '2024-01-15', 'Voi nuoc bon rua bat trong bep bi ri nuoc, can thay the voi moi', 'completed', 'medium', 4, '2024-01-16'),
('S1P0102', 3, '2024-01-20', 'Den LED phong khach bi chap chon, co the do bong den hong hoac van de dien', 'completed', 'high', 4, '2024-01-21');
