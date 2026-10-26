-- TODO 3: Chuẩn bị cơ sở dữ liệu SQL Server (StudentDB)
-- Mở SQL Server Management Studio (SSMS) và thực thi lệnh sau:

CREATE DATABASE StudentDB;
GO

USE StudentDB;
GO

-- Bảng dbo.Students sẽ được Hibernate tự động sinh theo cấu hình ddl-auto=update:
-- CREATE TABLE Students (
--     id             BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
--     student_code   VARCHAR(8)    NOT NULL UNIQUE,
--     full_name      NVARCHAR(50)  NOT NULL,
--     email          VARCHAR(100)  NOT NULL,
--     date_of_birth  DATE          NOT NULL,
--     gpa            FLOAT         NOT NULL,
--     major          VARCHAR(2)    NOT NULL,
--     active         BIT           NOT NULL
-- );
