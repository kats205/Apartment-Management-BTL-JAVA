-- Bảng Role (Vai trò)
CREATE TABLE Role (
    role_id INT PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    role_name VARCHAR(50) NOT NULL,
    description TEXT, 
);
-- Bảng User (Người dùng)
CREATE TABLE [User] (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
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
    position VARCHAR(50) NOT NULL, -- chức vụ của nhân viên
    hire_date DATE NOT NULL, -- ngày nhân viên được tuyển dụng
	manager_id INT, -- người quản lý trực tiếp nhân viên này
    FOREIGN KEY (staff_id) REFERENCES [User](user_id),
	FOREIGN KEY (manager_id) REFERENCES Manager(manager_id) 
);

-- Bảng Building (Tòa nhà)
CREATE TABLE Building (
    building_id INT PRIMARY KEY IDENTITY(1,1),
    building_name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
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
    floor INT NOT NULL CHECK(floor > 0), -- tầng = 0 là tầng trệt
    area FLOAT NOT NULL,
    bedrooms INT NOT NULL,
	price_apartment DECIMAL not null,
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
    identity_card VARCHAR(20) NOT NULL UNIQUE CHECK identity_card NOT LIKE '%[^0-9]%', -- chỉ cho phép số
    date_of_birth DATE,
    gender VARCHAR(10),
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
    price_sevrice DECIMAL NOT NULL,
    unit VARCHAR(20) NOT NULL, -- đơn vị thuê dịch vụ (tháng, năm, lượt)
    is_available BIT DEFAULT 1, -- quản lý trạng thái dịch vụ (1: đang được thuê, 0: chưa được thuê) 
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE() -- tạo trigger để tự động update
);

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
    description TEXT NOT NULL,
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
    content TEXT NOT NULL,
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
    report_type VARCHAR(50) NOT NULL,
    generation_date DATETIME2 NOT NULL,
    generated_by_user_id INT NOT NULL,
    parameters TEXT,
    file_path VARCHAR(255) NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (generated_by_user_id) REFERENCES [User](user_id)
);

--TRIGGER
