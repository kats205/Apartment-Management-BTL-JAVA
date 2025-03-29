-- Bảng Role (Vai trò)
CREATE TABLE Role (
    role_id INT PRIMARY KEY IDENTITY(1,1),
    role_name VARCHAR(50) NOT NULL,
    description NVARCHAR(MAX), 
);
-- Bảng User (Người dùng)
CREATE TABLE [User] (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE CHECK (email LIKE '%@%.%'),
    phone_number VARCHAR(20) CHECK (phone_number LIKE '0[0-9]%' AND LEN(phone_number) = 10),
    role_id INT NOT NULL, 
    active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(), -- dùng để lưu thời điểm tạo bản ghi
    updated_at DATETIME2 DEFAULT GETDATE(), -- dùng để lưu thời điểm khi có sự cập nhật bản ghi
    FOREIGN KEY (role_id) REFERENCES Role(role_id) -- role_id là khóa ngoại trong user để xác định role của 1 user
);


CREATE TABLE Manager(
	manager_id INT PRIMARY KEY,
	office NVARCHAR(100) NOT NULL,
	start_date DATE NOT NULL, -- ngày bổ nhiệm
	FOREIGN KEY (manager_id) REFERENCES [User](user_id)
)
-- Bảng Staff (Nhân viên) - Mở rộng từ User
CREATE TABLE Staff (
    staff_id INT PRIMARY KEY,
    department VARCHAR(50) NOT NULL, -- văn phòng làm việc của nhân viên
    position NVARCHAR(50) NOT NULL, -- chức vụ của nhân viên
    hire_date DATE NOT NULL, -- ngày nhân viên được tuyển dụng
	manager_id INT, -- người quản lý trực tiếp nhân viên này
	degree NVARCHAR(100) NOT NULL,
	certificate NVARCHAR(100) NOT NULL,
    FOREIGN KEY (staff_id) REFERENCES [User](user_id),
	FOREIGN KEY (manager_id) REFERENCES Manager(manager_id) 
);

-- Bảng Building (Tòa nhà)
CREATE TABLE Building (
    building_id INT PRIMARY KEY IDENTITY(1,1),
    building_name VARCHAR(100) NOT NULL,
    address NVARCHAR(MAX) NOT NULL,
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
    full_name NVARCHAR(100) NOT NULL,
    identity_card VARCHAR(20) NOT NULL UNIQUE CHECK (identity_card NOT LIKE '%[^0-9]%'), -- chỉ cho phép số
    date_of_birth DATE,
    gender NVARCHAR(10),
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
    description NVARCHAR(MAX),
    price_sevrice DECIMAL NOT NULL,
    unit VARCHAR(20) NOT NULL, -- đơn vị thuê dịch vụ (tháng, năm, lượt)
    is_available BIT DEFAULT 1, -- quản lý trạng thái dịch vụ (1: đang được thuê, 0: chưa được thuê) 
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE() -- tạo trigger để tự động update
);
drop table Service;
select * from [Service]
EXEC sp_rename 'Service.price_sevrice', 'price_service', 'COLUMN';
ALTER TABLE [Service]  
ALTER COLUMN price_service DECIMAL(10,2) NOT NULL;
-- Bảng ServiceRegistration (Đăng ký dịch vụ)
CREATE TABLE ServiceRegistration (
    registration_id INT PRIMARY KEY IDENTITY(1,1),
    service_id INT NOT NULL,
    apartment_id VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(20) NOT NULL CHECK (status IN ('active', 'cancelled', 'expired')),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (service_id) REFERENCES Service(service_id),
    FOREIGN KEY (apartment_id) REFERENCES Apartment(apartment_id)
);

-- Bảng Bill (Hóa đơn)
CREATE TABLE Bill (
    bill_id INT PRIMARY KEY IDENTITY(1,1),
    apartment_id VARCHAR(20) NOT NULL,
    billing_date DATE NOT NULL,
    due_date DATE NOT NULL,
    total_amount FLOAT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'paid', 'overdue', 'cancelled')),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
	late_fee DECIMAL(10,2) DEFAULT 0, -- phí trễ hạn của bill tính 10% cho mỗi ngày trễ hạn, nên tạo thêm trigger để tự động câp nhật phí trễ hạn
    FOREIGN KEY (apartment_id) REFERENCES Apartment(apartment_id)
);

-- Bảng BillItem (Chi tiết hóa đơn)
CREATE TABLE BillItem (
    item_id INT PRIMARY KEY IDENTITY(1,1),
    bill_id INT NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    description TEXT,
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
    amount FLOAT NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100),
    status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'completed', 'failed', 'refunded')),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (bill_id) REFERENCES Bill(bill_id)
);

-- Bảng MaintenanceRequest (Yêu cầu bảo trì)
CREATE TABLE MaintenanceRequest (
    request_id INT PRIMARY KEY IDENTITY(1,1),
    apartment_id VARCHAR(20) NOT NULL,
    resident_id INT NOT NULL,
    request_date DATE NOT NULL,
    description NVARCHAR(MAX) NOT NULL,
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
    content NVARCHAR(100) NOT NULL,
    creation_date DATETIME2 NOT NULL,
    type NVARCHAR(50) NOT NULL,
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
    report_type VARCHAR(50) NOT NULL,
    generation_date DATETIME2 NOT NULL,
    generated_by_user_id INT NOT NULL,
    parameters NVARCHAR(100),
    file_path VARCHAR(255) NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (generated_by_user_id) REFERENCES [User](user_id)
);
--INSERT 

INSERT INTO Role (role_name, description) VALUES 
('Manager', 'Quản lý hệ thống và tòa nhà'),
('Staff', 'Nhân viên hỗ trợ và vận hành'),
('Resident', 'Cư dân sử dụng căn hộ');
delete from [Role] where role_id in ( 1, 2, 3,4,5,6)
DBCC CHECKIDENT ('Role', RESEED, 0);

select * from [Role]
USE master;  
ALTER DATABASE QLCC SET SINGLE_USER WITH ROLLBACK IMMEDIATE;  
DROP DATABASE QLCC;  
use QLCC
go
create database QLCC;
-- Insert data into User table with the 3 roles
INSERT INTO [User] (username, password, full_name, email, phone_number, role_id) VALUES 
('quanly1', 'ManagerPass123!', N'Trần Văn Quản', 'quanly1@qlcc.com', '0901234567', 1),
('quanly2', 'ManagerPass456!', N'Lê Thị Giám', 'quanly2@qlcc.com', '0912345678', 1),
('nhanvien1', 'StaffPass123!', N'Phạm Văn Nhân', 'nhanvien1@qlcc.com', '0923456789', 2),
('nhanvien2', 'StaffPass456!', N'Hoàng Thị Viên', 'nhanvien2@qlcc.com', '0934567890', 2),
('nhanvien3', 'StaffPass789!', N'Võ Văn Hỗ', 'nhanvien3@qlcc.com', '0945678901', 2),
('cudan1', 'ResidentPass123!', N'Vũ Đức Cư', 'cudan1@qlcc.com', '0956789012', 3),
('cudan2', 'ResidentPass456!', N'Ngô Thị Dân', 'cudan2@qlcc.com', '0967890123', 3),
('cudan3', 'ResidentPass789!', N'Bùi Văn Nhà', 'cudan3@qlcc.com', '0978901234', 3),
('cudan4', 'ResidentPass012!', N'Đỗ Thị Hộ', 'cudan4@qlcc.com', '0989012345', 3),
('cudan5', 'ResidentPass345!', N'Trương Văn Chung', 'cudan5@qlcc.com', '0990123456', 3);
delete from [user]
select * from [User]
DBCC CHECKIDENT ('[User]', RESEED, 0);
-- Insert data into Manager table
INSERT INTO Manager (manager_id, office, start_date) VALUES 
(1, N'Văn phòng Điều Hành', '2022-01-15'),
(2, N'Văn phòng Quản Lý Tòa Nhà', '2021-11-01');

-- Insert data into Staff table
INSERT INTO Staff (staff_id, department, position, hire_date, manager_id) VALUES 
(3, N'Hành Chính', N'Nhân Viên Hành Chính', '2023-02-01', 1),
(4, N'Vận Hành', N'Nhân Viên Kỹ Thuật', '2022-12-15', 2),
(5, N'Dịch Vụ', N'Nhân Viên Dịch Vụ', '2022-08-10', 1);

-- Insert data into Resident table
INSERT INTO Resident (apartment_id, full_name, identity_card, date_of_birth, gender, user_id, is_primary_resident, move_in_date) VALUES 
('A101', N'Vũ Đức Cư', '001234567890', '1985-05-15', N'Nam', 6, 1, '2022-06-01'),
('A102', N'Ngô Thị Dân', '002345678901', '1990-08-20', N'Nữ', 7, 1, '2022-07-15'),
('B201', N'Bùi Văn Nhà', '003456789012', '1978-03-10', N'Nam', 8, 1, '2022-09-01'),
('B202', N'Đỗ Thị Hộ', '004567890123', '1982-11-25', N'Nữ', 9, 1, '2022-10-15'),
('C301', N'Trương Văn Chung', '005678901234', '1975-07-30', N'Nam', 10, 1, '2022-12-01');
select * from Resident;
DELETE FROM Resident;
DBCC CHECKIDENT ('Resident', RESEED, 0);

select * from Resident 
INSERT INTO Building (building_name, address, total_floors, total_apartments, completion_date) VALUES 
(N'Chung Cư Xanh', N'123 Đường Lê Văn Lương, Quận 7, TP.HCM', 15, 180, '2020-12-31'),
(N'Tháp Sen', N'456 Đường Nguyễn Văn Trỗi, Quận Phú Nhuận, TP.HCM', 20, 240, '2021-06-30'),
(N'Khu Dân Cư Hạnh Phúc', N'789 Đường Cộng Hòa, Quận Tân Bình, TP.HCM', 12, 150, '2019-09-15'),
(N'Chung Cư Thiên Đường', N'101 Đường Trường Chinh, Quận 12, TP.HCM', 18, 220, '2021-03-20'),
(N'Bắc Linh Đàm Tower', N'202 Đường Điện Biên Phủ, Quận Bình Thạnh, TP.HCM', 22, 280, '2022-01-10'),
(N'Goldmark City', N'88 Đường Hồng Tiến, Quận Long Biên, Hà Nội', 25, 300, '2021-11-05'),
(N'Imperia Sky Garden', N'360 Đường Giải Phóng, Quận Hoàng Mai, Hà Nội', 16, 190, '2020-08-25'),
(N'Royal City', N'72 Nguyễn Trãi, Quận Thanh Xuân, Hà Nội', 30, 350, '2022-05-15'),
(N'Vinhomes Central Park', N'720A Điện Biên Phủ, Quận Bình Thạnh, TP.HCM', 40, 450, '2021-07-20'),
(N'Sun Grand City', N'69 Thụy Khuê, Quận Tây Hồ, Hà Nội', 12, 140, '2020-04-30'),
(N'New Horizon City', N'234 Đào Duy Anh, Quận Đống Đa, Hà Nội', 15, 180, '2021-09-10'),
(N'Eco Lake View', N'32 Đại Từ, Quận Hoàng Mai, Hà Nội', 18, 220, '2022-02-15'),
(N'Times City', N'458 Minh Khai, Quận Hai Bà Trưng, Hà Nội', 25, 300, '2021-05-20'),
(N'Park Premium', N'99 Trần Duy Hưng, Quận Cầu Giấy, Hà Nội', 20, 240, '2020-11-15'),
(N'The Manor Central Park', N'177 Hồ Tùng Mậu, Quận Nam Từ Liêm, Hà Nội', 35, 420, '2022-03-25');

-- Insert data into Apartment table
INSERT INTO Apartment (apartment_id, building_id, floor, area, bedrooms, price_apartment, status, maintenance_fee) VALUES 
('A101', 1, 1, 75.5, 2, 1510000000, 'occupied', 1500000),
('A102', 1, 1, 65.2, 2, 1304000000, 'available', 1300000),
('B201', 2, 2, 80.3, 3, 1766600000, 'available', 1600000),
('B202', 2, 2, 70.1, 2, 1331900000, 'maintenance', 1400000),
('C301', 3, 3, 90.5, 3, 1991000000, 'reserved', 1800000),
('C302', 3, 3, 60.7, 1, 1214000000, 'available', 1200000),
('D401', 4, 4, 85.6, 3, 1883200000, 'occupied', 1700000),
('D402', 4, 4, 72.4, 2, 1448000000, 'available', 1450000),
('E501', 5, 5, 95.2, 3, 2094400000, 'available', 1900000),
('E502', 5, 5, 68.9, 2, 1377800000, 'maintenance', 1350000),
('F601', 6, 6, 78.3, 2, 1566000000, 'reserved', 1550000),
('F602', 6, 6, 66.5, 1, 1330000000, 'available', 1250000),
('G701', 7, 7, 82.7, 3, 1819400000, 'occupied', 1650000),
('G702', 7, 7, 69.8, 2, 1396000000, 'available', 1400000),
('H801', 8, 8, 88.4, 3, 1944800000, 'available', 1750000),
('I901', 9, 9, 76.2, 2, 1524000000, 'reserved', 1500000),
('J1001', 10, 10, 92.6, 3, 2037200000, 'maintenance', 1850000),
('K1101', 11, 11, 67.8, 2, 1356000000, 'occupied', 1350000),
('L1201', 12, 12, 84.5, 3, 1862000000, 'available', 1700000),
('M1301', 13, 13, 71.3, 2, 1426000000, 'reserved', 1450000);
--Công thức chính: price_apartment = area × 20,000,000
--Thêm 10% nếu có 3 phòng ngủ (bedrooms = 3)
--Giảm 5% nếu trạng thái là "maintenance" hoặc "reserved"`
select * from apartment



-- Insert data into Service table
INSERT INTO Service (service_name, description, price_sevrice, unit, is_available) VALUES 
(N'Giữ xe ô tô', N'Dịch vụ giữ xe ô tô an toàn', 500000, N'tháng', 1),
(N'Giặt ủi', N'Dịch vụ giặt ủi chuyên nghiệp', 300000, N'tháng', 1),
(N'Vệ sinh', N'Dịch vụ vệ sinh căn hộ', 200000, N'tháng', 1),
(N'Internet', N'Dịch vụ internet tốc độ cao', 150000, N'tháng', 1),
(N'Bảo vệ 24/7', N'Dịch vụ bảo vệ toàn thời gian', 100000, N'tháng', 1),
(N'Phòng tập gym', N'Dịch vụ phòng tập hiện đại', 350000, N'tháng', 1),
(N'Hồ bơi', N'Dịch vụ hồ bơi cao cấp', 250000, N'tháng', 1),
(N'Dọn nhà', N'Dịch vụ dọn nhà chuyên nghiệp', 150000, N'lượt', 1),
(N'Sửa chữa điện nước', N'Dịch vụ sửa chữa khẩn cấp', 300000, N'lượt', 1),
(N'Diệt côn trùng', N'Dịch vụ diệt côn trùng', 500000, N'lượt', 1),
(N'Camera an ninh', N'Dịch vụ camera an ninh', 200000, N'tháng', 1),
(N'Quản lý chung cư', N'Dịch vụ quản lý chung cư', 50000, N'tháng', 1),
(N'Thu gom rác', N'Dịch vụ thu gom rác', 100000, N'tháng', 1),
(N'Chăm sóc cây xanh', N'Dịch vụ chăm sóc cảnh quan', 150000, N'tháng', 1),
(N'Bảo trì thang máy', N'Dịch vụ bảo trì thang máy', 300000, N'tháng', 1);

-- Insert data into ServiceRegistration table
INSERT INTO ServiceRegistration (service_id, apartment_id, start_date, end_date, status) VALUES 
(1, 'A101', '2023-01-01', '2023-12-31', 'active'),
(2, 'A101', '2023-02-15', '2023-08-15', 'active'),
(3, 'A102', '2023-03-01', '2023-09-01', 'active'),
(4, 'B201', '2023-01-15', '2023-07-15', 'active'),
(5, 'B202', '2023-04-01', '2023-10-01', 'active'),
(6, 'C301', '2023-02-01', '2023-08-01', 'active'),
(7, 'C302', '2023-03-15', '2023-09-15', 'active'),
(8, 'D401', '2023-01-01', '2023-12-31', 'active'),
(9, 'D402', '2023-02-15', '2023-08-15', 'active'),
(10, 'E501', '2023-03-01', '2023-09-01', 'active'),
(11, 'E502', '2023-01-15', '2023-07-15', 'active'),
(12, 'F601', '2023-04-01', '2023-10-01', 'active'),
(13, 'F602', '2023-02-01', '2023-08-01', 'active'),
(14, 'G701', '2023-03-15', '2023-09-15', 'active'),
(15, 'G702', '2023-01-01', '2023-12-31', 'active');

-- Insert data into Bill table
INSERT INTO Bill (apartment_id, billing_date, due_date, total_amount, status, late_fee) VALUES 
('A101', '2023-05-01', '2023-05-15', 2500000, 'pending', 0),
('A102', '2023-05-01', '2023-05-15', 1800000, 'pending', 0),
('B201', '2023-05-01', '2023-05-15', 3200000, 'pending', 0),
('B202', '2023-05-01', '2023-05-15', 2100000, 'pending', 0),
('C301', '2023-05-01', '2023-05-15', 2800000, 'pending', 0),
('C302', '2023-05-01', '2023-05-15', 1600000, 'pending', 0),
('D401', '2023-05-01', '2023-05-15', 3500000, 'pending', 0),
('D402', '2023-05-01', '2023-05-15', 2300000, 'pending', 0),
('E501', '2023-05-01', '2023-05-15', 3000000, 'pending', 0),
('E502', '2023-05-01', '2023-05-15', 1900000, 'pending', 0),
('F601', '2023-05-01', '2023-05-15', 2600000, 'pending', 0),
('F602', '2023-05-01', '2023-05-15', 1700000, 'pending', 0),
('G701', '2023-05-01', '2023-05-15', 3300000, 'pending', 0),
('G702', '2023-05-01', '2023-05-15', 2200000, 'pending', 0),
('H801', '2023-05-01', '2023-05-15', 2700000, 'pending', 0);

-- Insert data into BillItem table
INSERT INTO BillItem (bill_id, item_type, description, amount, quantity, total) VALUES 
(1, N'Phí quản lý', N'Phí quản lý chung cư', 500000, 1, 500000),
(1, N'Phí dịch vụ', N'Phí giữ xe', 1000000, 1, 1000000),
(1, N'Tiền điện', N'Tiêu thụ điện', 1000000, 1, 1000000),
(2, N'Phí quản lý', N'Phí quản lý chung cư', 300000, 1, 300000),
(2, N'Tiền nước', N'Tiêu thụ nước', 500000, 1, 500000),
(2, N'Internet', N'Dịch vụ internet', 1000000, 1, 1000000),
(3, N'Phí quản lý', N'Phí quản lý chung cư', 600000, 1, 600000),
(3, N'Phí dịch vụ', N'Phí sử dụng gym', 1200000, 1, 1200000),
(3, N'Tiền điện', N'Tiêu thụ điện', 1400000, 1, 1400000),
(4, N'Phí quản lý', N'Phí quản lý chung cư', 400000, 1, 400000),
(4, N'Tiền nước', N'Tiêu thụ nước', 700000, 1, 700000),
(4, N'Bảo trì', N'Phí bảo trì', 1000000, 1, 1000000),
(5, N'Phí quản lý', N'Phí quản lý chung cư', 500000, 1, 500000),
(5, N'Phí dịch vụ', N'Phí hồ bơi', 1300000, 1, 1300000),
(5, N'Tiền điện', N'Tiêu thụ điện', 1000000, 1, 1000000);
select * from BillItem
-- Insert data into Payment table
INSERT INTO Payment (bill_id, amount, payment_date, payment_method, transaction_id, status) VALUES 
(1, 2500000, '2023-05-10', N'Chuyển khoản', 'TRX123456', 'pending'),
(2, 1800000, '2023-05-12', N'Tiền mặt', 'CSH789012', 'pending'),
(3, 3200000, '2023-05-08', N'Thẻ ngân hàng', 'CC345678', 'pending'),
(4, 2100000, '2023-05-15', N'Chuyển khoản', 'TRX901234', 'pending'),
(5, 2800000, '2023-05-11', N'Ví điện tử', 'E-WALLET567890', 'pending'),
(6, 1600000, '2023-05-13', N'Chuyển khoản', 'TRX234567', 'pending'),
(7, 3500000, '2023-05-09', N'Tiền mặt', 'CSH345678', 'pending'),
(8, 2300000, '2023-05-14', N'Thẻ ngân hàng', 'CC456789', 'pending'),
(9, 3000000, '2023-05-12', N'Ví điện tử', 'E-WALLET678901', 'pending'),
(10, 1900000, '2023-05-10', N'Chuyển khoản', 'TRX789012', 'pending'),
(11, 2600000, '2023-05-13', N'Tiền mặt', 'CSH456789', 'pending'),
(12, 1700000, '2023-05-11', N'Thẻ ngân hàng', 'CC567890', 'pending'),
(13, 3300000, '2023-05-14', N'Chuyển khoản', 'TRX890123', 'pending'),
(14, 2200000, '2023-05-09', N'Ví điện tử', 'E-WALLET901234', 'pending'),
(15, 2700000, '2023-05-15', N'Tiền mặt', 'CSH567890', 'pending');
select * from Payment
delete from Payment where payment_id between 16 and 30 
-- Insert data into MaintenanceRequest table
INSERT INTO MaintenanceRequest (apartment_id, resident_id, request_date, description, status, priority, assigned_staff_id, completion_date) VALUES 
('A101', 1, '2023-05-02', N'Hỏng khóa cửa', 'pending', 'medium', 3, NULL),
('A102', 2, '2023-05-05', N'Rò rỉ nước', 'assigned', 'high', 4, NULL),
('B201', 1, '2023-05-07', N'Bóng đèn không sáng', 'in_progress', 'low', 5, NULL),
('B202', 2, '2023-05-10', N'Điều hòa không mát', 'pending', 'high', 3, NULL),
('C301', 1, '2023-05-12', N'Hệ thống điện yếu', 'assigned', 'urgent', 4, NULL),
('C302', 2, '2023-05-15', N'Sửa ống nước', 'in_progress', 'medium', 5, NULL),
('D401', 1, '2023-05-03', N'Thang máy kêu', 'completed', 'low', 3, '2023-05-07'),
('D402', 2, '2023-05-06', N'Khung cửa sổ lỏng', 'assigned', 'medium', 4, NULL),
('E501', 1, '2023-05-08', N'Vệ sinh máy lạnh', 'pending', 'low', 5, NULL),
('E502', 2, '2023-05-11', N'Bể nước nóng', 'in_progress', 'high', 3, NULL);
select * from MaintenanceRequest
delete from MaintenanceRequest

-- Insert data into Notification table
INSERT INTO Notification (title, content, creation_date, type) VALUES 
(N'Thông Báo Bảo Trì', N'Sẽ có đợt bảo trì thang máy vào tuần tới', '2023-05-01', 'maintenance'),
(N'Sự Kiện Cộng Đồng', N'Chào mừng ngày quốc tế thiếu nhi', '2023-05-30', 'community'),
(N'Cảnh Báo An Ninh', N'Yêu cầu tăng cường cảnh giác', '2023-05-15', 'security'),
(N'Thông Tin Dịch Vụ', N'Cập nhật các dịch vụ mới', '2023-06-01', 'service'),
(N'Hướng Dẫn Thanh Toán', N'Hướng dẫn thanh toán trực tuyến', '2023-05-20', 'payment'),
(N'Hoạt Động Môi Trường', N'Chương trình làm sạch khu chung cư', '2023-05-25', 'environment'),
(N'Thông Báo Sự Kiện', N'Tiệc cuối năm cho cư dân', '2023-06-15', 'event'),
(N'Tin Tức Quản Lý', N'Thay đổi quy định quản lý', '2023-05-10', 'management'),
(N'Khuyến Mãi Dịch Vụ', N'Ưu đãi dịch vụ giặt ủi', '2023-05-05', 'promotion'),
(N'Bảo Trì Định Kỳ', N'Lịch bảo trì hệ thống', '2023-05-18', 'maintenance');
select * from [Notification]
DELETE FROM [Notification] WHERE notification_id BETWEEN 11 AND 20;

-- Insert data into NotificationRecipient table
INSERT INTO NotificationRecipient (notification_id, user_id, is_read, read_at) VALUES 
(1, 6, 0, NULL),
(1, 7, 0, NULL),
(2, 6, 0, NULL),
(2, 7, 0, NULL),
(3, 6, 1, '2023-05-16 10:30:00'),
(3, 7, 0, NULL),
(4, 6, 0, NULL),
(4, 7, 0, NULL),
(5, 6, 1, '2023-05-21 15:45:00'),
(5, 7, 0, NULL);
select * from NotificationRecipient
-- Insert data into Report table
INSERT INTO Report (report_type, generation_date, generated_by_user_id, parameters, file_path) VALUES 
(N'Báo Cáo Tài Chính', '2023-05-01', 1, N'Tháng 4/2023', '/reports/financial_04_2023.pdf'),
(N'Báo Cáo Dịch Vụ', '2023-05-05', 2, N'Quý 2/2023', '/reports/service_q2_2023.pdf'),
(N'Báo Cáo Bảo Trì', '2023-05-10', 3, N'Tháng 5/2023', '/reports/maintenance_05_2023.pdf'),
(N'Báo Cáo Cư Dân', '2023-05-15', 1, N'Tháng 5/2023', '/reports/resident_05_2023.pdf'),
(N'Báo Cáo An Ninh', '2023-05-20', 2, N'Tháng 5/2023', '/reports/security_05_2023.pdf');

--TRIGGER
