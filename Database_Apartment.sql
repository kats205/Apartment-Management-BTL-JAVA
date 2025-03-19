-- Tạo cơ sở dữ liệu quản lý chung cư
CREATE DATABASE ApartmentManagement;
USE ApartmentManagement;

-- Bảng vai trò người dùng
CREATE TABLE UserRoles (
    RoleID INT PRIMARY KEY,
    RoleName NVARCHAR(50) NOT NULL,
    Description NVARCHAR(200)
);

-- Bảng người dùng (nhân viên quản lý, admin)
CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    Username NVARCHAR(50) NOT NULL UNIQUE,
    Password NVARCHAR(100) NOT NULL, -- Nên lưu mật khẩu đã mã hóa
    FullName NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100),
    Phone NVARCHAR(20),
    RoleID INT NOT NULL,
    IsActive BIT DEFAULT 1,
    CreatedDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (RoleID) REFERENCES UserRoles(RoleID)
);

-- Bảng tòa nhà
CREATE TABLE Buildings (
    BuildingID INT PRIMARY KEY IDENTITY(1,1),
    BuildingName NVARCHAR(50) NOT NULL,
    Address NVARCHAR(200) NOT NULL,
    TotalFloors INT NOT NULL,
    YearBuilt INT,
    Description NVARCHAR(500)
);

-- Bảng tầng
CREATE TABLE Floors (
    FloorID INT PRIMARY KEY IDENTITY(1,1),
    BuildingID INT NOT NULL,
    FloorNumber INT NOT NULL,
    Description NVARCHAR(200),
    FOREIGN KEY (BuildingID) REFERENCES Buildings(BuildingID),
    CONSTRAINT UQ_Floor UNIQUE (BuildingID, FloorNumber)
);

-- Bảng căn hộ
CREATE TABLE Apartments (
    ApartmentID INT PRIMARY KEY IDENTITY(1,1),
    ApartmentNumber NVARCHAR(20) NOT NULL,
    FloorID INT NOT NULL,
    BuildingID INT NOT NULL,
    Area DECIMAL(10,2) NOT NULL, -- Diện tích (m2)
    Bedrooms INT NOT NULL,
    Bathrooms INT NOT NULL,
    Status NVARCHAR(20) NOT NULL, -- Trạng thái: Đã bán, Đang cho thuê, Trống
    PurchasePrice DECIMAL(18,2), -- Giá mua
    MonthlyFee DECIMAL(18,2), -- Phí quản lý hàng tháng
    Description NVARCHAR(500),
    FOREIGN KEY (FloorID) REFERENCES Floors(FloorID),
    FOREIGN KEY (BuildingID) REFERENCES Buildings(BuildingID),
    CONSTRAINT UQ_Apartment UNIQUE (BuildingID, ApartmentNumber)
);

-- Bảng cư dân
CREATE TABLE Residents (
    ResidentID INT PRIMARY KEY IDENTITY(1,1),
    FullName NVARCHAR(100) NOT NULL,
    DateOfBirth DATE,
    Gender NVARCHAR(10),
    IdentityCard NVARCHAR(20) NOT NULL UNIQUE,
    Phone NVARCHAR(20),
    Email NVARCHAR(100),
    EmergencyContact NVARCHAR(100),
    EmergencyPhone NVARCHAR(20),
    Username NVARCHAR(50) UNIQUE, -- Tài khoản đăng nhập
    Password NVARCHAR(100), -- Mật khẩu đã mã hóa
    IsActive BIT DEFAULT 1,
    RegisterDate DATETIME DEFAULT GETDATE()
);

-- Bảng sở hữu căn hộ
CREATE TABLE ApartmentOwnership (
    OwnershipID INT PRIMARY KEY IDENTITY(1,1),
    ApartmentID INT NOT NULL,
    ResidentID INT NOT NULL,
    OwnershipType NVARCHAR(20) NOT NULL, -- Loại: Chủ sở hữu, Người thuê
    StartDate DATE NOT NULL,
    EndDate DATE, -- NULL nếu là chủ sở hữu vĩnh viễn
    ContractNumber NVARCHAR(50),
    Notes NVARCHAR(500),
    FOREIGN KEY (ApartmentID) REFERENCES Apartments(ApartmentID),
    FOREIGN KEY (ResidentID) REFERENCES Residents(ResidentID)
);

-- Bảng thành viên gia đình
CREATE TABLE FamilyMembers (
    MemberID INT PRIMARY KEY IDENTITY(1,1),
    ResidentID INT NOT NULL, -- Liên kết với chủ hộ
    FullName NVARCHAR(100) NOT NULL,
    DateOfBirth DATE,
    Gender NVARCHAR(10),
    Relationship NVARCHAR(50) NOT NULL, -- Quan hệ với chủ hộ
    IdentityCard NVARCHAR(20) UNIQUE,
    Phone NVARCHAR(20),
    FOREIGN KEY (ResidentID) REFERENCES Residents(ResidentID)
);

-- Bảng xe của cư dân
CREATE TABLE Vehicles (
    VehicleID INT PRIMARY KEY IDENTITY(1,1),
    ResidentID INT NOT NULL,
    VehicleType NVARCHAR(20) NOT NULL, -- Loại: Ô tô, Xe máy
    LicensePlate NVARCHAR(20) NOT NULL UNIQUE,
    Brand NVARCHAR(50),
    Model NVARCHAR(50),
    Color NVARCHAR(20),
    ParkingSlot NVARCHAR(20),
    RegisterDate DATE,
    FOREIGN KEY (ResidentID) REFERENCES Residents(ResidentID)
);

-- Bảng phí dịch vụ
CREATE TABLE ServiceFees (
    FeeID INT PRIMARY KEY IDENTITY(1,1),
    FeeName NVARCHAR(100) NOT NULL,
    Description NVARCHAR(500),
    DefaultAmount DECIMAL(18,2),
    Unit NVARCHAR(20), -- Đơn vị tính: m2, người, căn hộ
    IsActive BIT DEFAULT 1
);

-- Bảng hóa đơn hàng tháng
CREATE TABLE MonthlyBills (
    BillID INT PRIMARY KEY IDENTITY(1,1),
    ApartmentID INT NOT NULL,
    Month INT NOT NULL,
    Year INT NOT NULL,
    TotalAmount DECIMAL(18,2) NOT NULL,
    IssueDate DATE NOT NULL,
    DueDate DATE NOT NULL,
    PaymentStatus NVARCHAR(20) DEFAULT 'Chưa thanh toán',
    PaymentDate DATE,
    Notes NVARCHAR(500),
    FOREIGN KEY (ApartmentID) REFERENCES Apartments(ApartmentID),
    CONSTRAINT UQ_MonthlyBill UNIQUE (ApartmentID, Month, Year)
);

-- Bảng chi tiết hóa đơn
CREATE TABLE BillDetails (
    DetailID INT PRIMARY KEY IDENTITY(1,1),
    BillID INT NOT NULL,
    FeeID INT NOT NULL,
    Quantity DECIMAL(10,2) NOT NULL,
    UnitPrice DECIMAL(18,2) NOT NULL,
    Amount DECIMAL(18,2) NOT NULL,
    Notes NVARCHAR(200),
    FOREIGN KEY (BillID) REFERENCES MonthlyBills(BillID),
    FOREIGN KEY (FeeID) REFERENCES ServiceFees(FeeID)
);

-- Bảng sự cố/báo cáo
CREATE TABLE Incidents (
    IncidentID INT PRIMARY KEY IDENTITY(1,1),
    ApartmentID INT NOT NULL,
    ReportedBy INT NOT NULL, -- ResidentID
    IncidentType NVARCHAR(50) NOT NULL,
    Description NVARCHAR(500) NOT NULL,
    ReportDate DATETIME DEFAULT GETDATE(),
    Status NVARCHAR(20) DEFAULT 'Đang xử lý',
    AssignedTo INT, -- UserID (nhân viên xử lý)
    ResolvedDate DATETIME,
    Resolution NVARCHAR(500),
    FOREIGN KEY (ApartmentID) REFERENCES Apartments(ApartmentID),
    FOREIGN KEY (ReportedBy) REFERENCES Residents(ResidentID),
    FOREIGN KEY (AssignedTo) REFERENCES Users(UserID)
);

-- Bảng quyền truy cập
CREATE TABLE Permissions (
    PermissionID INT PRIMARY KEY IDENTITY(1,1),
    PermissionName NVARCHAR(50) NOT NULL UNIQUE,
    Description NVARCHAR(200)
);

-- Bảng phân quyền cho vai trò
CREATE TABLE RolePermissions (
    RoleID INT NOT NULL,
    PermissionID INT NOT NULL,
    PRIMARY KEY (RoleID, PermissionID),
    FOREIGN KEY (RoleID) REFERENCES UserRoles(RoleID),
    FOREIGN KEY (PermissionID) REFERENCES Permissions(PermissionID)
);

-- Bảng thông báo
CREATE TABLE Announcements (
    AnnouncementID INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(100) NOT NULL,
    Content NVARCHAR(MAX) NOT NULL,
    CreatedBy INT NOT NULL, -- UserID
    CreatedDate DATETIME DEFAULT GETDATE(),
    ExpireDate DATE,
    IsPublic BIT DEFAULT 1, -- Thông báo công khai hay không
    BuildingID INT, -- NULL nếu áp dụng cho tất cả các tòa nhà
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
    FOREIGN KEY (BuildingID) REFERENCES Buildings(BuildingID)
);

-- Chèn dữ liệu mẫu cho vai trò
INSERT INTO UserRoles (RoleID, RoleName, Description) VALUES
(1, 'Admin', 'Quản trị viên hệ thống'),
(2, 'Manager', 'Quản lý chung cư'),
(3, 'Staff', 'Nhân viên chung cư'),
(4, 'Resident', 'Cư dân');

-- Chèn dữ liệu mẫu cho quyền truy cập
INSERT INTO Permissions (PermissionName, Description) VALUES
('VIEW_ALL_APARTMENTS', 'Xem tất cả căn hộ'),
('MANAGE_APARTMENTS', 'Quản lý căn hộ'),
('VIEW_ALL_RESIDENTS', 'Xem tất cả cư dân'),
('MANAGE_RESIDENTS', 'Quản lý cư dân'),
('VIEW_ALL_BILLS', 'Xem tất cả hóa đơn'),
('MANAGE_BILLS', 'Quản lý hóa đơn'),
('VIEW_OWN_BILLS', 'Cư dân xem hóa đơn của mình'),
('MANAGE_INCIDENTS', 'Quản lý sự cố'),
('REPORT_INCIDENTS', 'Báo cáo sự cố'),
('MANAGE_USERS', 'Quản lý người dùng'),
('MANAGE_ANNOUNCEMENTS', 'Quản lý thông báo'),
('VIEW_REPORTS', 'Xem báo cáo thống kê');

-- Phân quyền cho vai trò
INSERT INTO RolePermissions (RoleID, PermissionID) VALUES
-- Admin có tất cả quyền
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12),
-- Manager có quyền quản lý nhưng không có quyền quản lý người dùng
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8), (2, 9), (2, 11), (2, 12),
-- Staff có quyền xem và báo cáo
(3, 1), (3, 3), (3, 5), (3, 7), (3, 8), (3, 9), (3, 11),
-- Resident chỉ có quyền xem thông tin của mình và báo cáo sự cố
(4, 7), (4, 9);

-- Tạo view cho quản lý căn hộ
CREATE VIEW vw_ApartmentDetails AS
SELECT 
    a.ApartmentID,
    a.ApartmentNumber,
    b.BuildingName,
    f.FloorNumber,
    a.Area,
    a.Bedrooms,
    a.Bathrooms,
    a.Status,
    a.PurchasePrice,
    a.MonthlyFee,
    r.FullName AS CurrentOwner,
    ao.OwnershipType,
    ao.StartDate AS OccupationStartDate
FROM 
    Apartments a
    JOIN Buildings b ON a.BuildingID = b.BuildingID
    JOIN Floors f ON a.FloorID = f.FloorID
    LEFT JOIN ApartmentOwnership ao ON a.ApartmentID = ao.ApartmentID AND (ao.EndDate IS NULL OR ao.EndDate > GETDATE())
    LEFT JOIN Residents r ON ao.ResidentID = r.ResidentID;

-- Tạo view cho quản lý cư dân
CREATE VIEW vw_ResidentDetails AS
SELECT 
    r.ResidentID,
    r.FullName,
    r.Phone,
    r.Email,
    a.ApartmentNumber,
    b.BuildingName,
    ao.OwnershipType,
    ao.StartDate,
    ao.EndDate,
    COUNT(fm.MemberID) AS FamilyMemberCount
FROM 
    Residents r
    JOIN ApartmentOwnership ao ON r.ResidentID = ao.ResidentID
    JOIN Apartments a ON ao.ApartmentID = a.ApartmentID
    JOIN Buildings b ON a.BuildingID = b.BuildingID
    LEFT JOIN FamilyMembers fm ON r.ResidentID = fm.ResidentID
GROUP BY 
    r.ResidentID, r.FullName, r.Phone, r.Email, a.ApartmentNumber, b.BuildingName, ao.OwnershipType, ao.StartDate, ao.EndDate;

-- Tạo view cho quản lý hóa đơn
CREATE VIEW vw_BillSummary AS
SELECT 
    mb.BillID,
    a.ApartmentNumber,
    b.BuildingName,
    r.FullName AS ResidentName,
    mb.Month,
    mb.Year,
    mb.TotalAmount,
    mb.IssueDate,
    mb.DueDate,
    mb.PaymentStatus,
    mb.PaymentDate
FROM 
    MonthlyBills mb
    JOIN Apartments a ON mb.ApartmentID = a.ApartmentID
    JOIN Buildings b ON a.BuildingID = b.BuildingID
    LEFT JOIN ApartmentOwnership ao ON a.ApartmentID = ao.ApartmentID AND (ao.EndDate IS NULL OR ao.EndDate > GETDATE())
    LEFT JOIN Residents r ON ao.ResidentID = r.ResidentID;

-- Tạo stored procedure để kiểm tra quyền truy cập
CREATE PROCEDURE sp_CheckUserPermission
    @UserID INT,
    @PermissionName NVARCHAR(50)
AS
BEGIN
    DECLARE @HasPermission BIT = 0;
    
    SELECT @HasPermission = 1
    FROM Users u
    JOIN RolePermissions rp ON u.RoleID = rp.RoleID
    JOIN Permissions p ON rp.PermissionID = p.PermissionID
    WHERE u.UserID = @UserID AND p.PermissionName = @PermissionName;
    
    RETURN @HasPermission;
END;

-- Tạo stored procedure để tạo hóa đơn hàng tháng
CREATE PROCEDURE sp_GenerateMonthlyBills
    @Month INT,
    @Year INT,
    @DueDate DATE
AS
BEGIN
    -- Tạo hóa đơn cho từng căn hộ
    INSERT INTO MonthlyBills (ApartmentID, Month, Year, TotalAmount, IssueDate, DueDate, PaymentStatus)
    SELECT 
        a.ApartmentID,
        @Month,
        @Year,
        a.MonthlyFee, -- Giá trị cơ bản, sẽ được cập nhật sau
        GETDATE(),
        @DueDate,
        'Chưa thanh toán'
    FROM 
        Apartments a
    WHERE 
        NOT EXISTS (
            SELECT 1 FROM MonthlyBills mb 
            WHERE mb.ApartmentID = a.ApartmentID AND mb.Month = @Month AND mb.Year = @Year
        );
    
    -- Thêm chi tiết hóa đơn cho từng loại phí
    INSERT INTO BillDetails (BillID, FeeID, Quantity, UnitPrice, Amount)
    SELECT 
        mb.BillID,
        sf.FeeID,
        CASE 
            WHEN sf.Unit = 'm2' THEN a.Area
            WHEN sf.Unit = 'căn hộ' THEN 1
            ELSE 1 -- Mặc định
        END AS Quantity,
        sf.DefaultAmount AS UnitPrice,
        CASE 
            WHEN sf.Unit = 'm2' THEN a.Area * sf.DefaultAmount
            WHEN sf.Unit = 'căn hộ' THEN sf.DefaultAmount
            ELSE sf.DefaultAmount
        END AS Amount
    FROM 
        MonthlyBills mb
        JOIN Apartments a ON mb.ApartmentID = a.ApartmentID
        CROSS JOIN ServiceFees sf
    WHERE 
        mb.Month = @Month AND mb.Year = @Year AND sf.IsActive = 1;
    
    -- Cập nhật tổng số tiền cho từng hóa đơn
    UPDATE mb
    SET TotalAmount = (SELECT SUM(Amount) FROM BillDetails bd WHERE bd.BillID = mb.BillID)
    FROM MonthlyBills mb
    WHERE mb.Month = @Month AND mb.Year = @Year;
END;

-- Tạo trigger để ghi nhật ký hoạt động
CREATE TABLE ActivityLogs (
    LogID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Action NVARCHAR(100) NOT NULL,
    TableName NVARCHAR(50) NOT NULL,
    RecordID INT,
    LogDate DATETIME DEFAULT GETDATE(),
    Description NVARCHAR(500),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Trigger ghi nhật ký khi thêm căn hộ mới
CREATE TRIGGER trg_ApartmentInsert
ON Apartments
AFTER INSERT
AS
BEGIN
    INSERT INTO ActivityLogs (UserID, Action, TableName, RecordID, Description)
    SELECT 
        NULL, -- UserID sẽ được cập nhật từ context của người dùng trong ứng dụng
        'INSERT',
        'Apartments',
        i.ApartmentID,
        'Thêm mới căn hộ ' + i.ApartmentNumber
    FROM inserted i;
END;