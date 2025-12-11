-- =========================
-- USER TABLE
-- =========================
CREATE TABLE `User` (
                        userId INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(100) NOT NULL UNIQUE,
                        hashedPwd VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- =========================
-- GROUP TABLE
-- =========================
CREATE TABLE `Group` (
                         groupId INT AUTO_INCREMENT PRIMARY KEY,
                         groupName VARCHAR(100) NOT NULL,
                         creatorId INT NOT NULL,

                         CONSTRAINT fk_group_creator
                             FOREIGN KEY (creatorId)
                                 REFERENCES `User`(userId)
                                 ON DELETE CASCADE
) ENGINE=InnoDB;

-- =========================
-- USERGROUP (MANY-TO-MANY)
-- =========================
CREATE TABLE UserGroup (
                           uniqueId INT AUTO_INCREMENT PRIMARY KEY,
                           userId INT NOT NULL,
                           groupId INT NOT NULL,

                           CONSTRAINT fk_usergroup_user
                               FOREIGN KEY (userId)
                                   REFERENCES `User`(userId)
                                   ON DELETE CASCADE,

                           CONSTRAINT fk_usergroup_group
                               FOREIGN KEY (groupId)
                                   REFERENCES `Group`(groupId)
                                   ON DELETE CASCADE,

                           UNIQUE (userId, groupId)
) ENGINE=InnoDB;

-- =========================
-- PASSWORD TABLE
-- =========================
CREATE TABLE Password (
                          passwordId INT AUTO_INCREMENT PRIMARY KEY,
                          website_app VARCHAR(255) NOT NULL,
                          loginName VARCHAR(255) NOT NULL,
                          encryptedPwd VARBINARY(255) NOT NULL,
                          note TEXT,
                          tag VARCHAR(100),
                          securityTag VARCHAR(100),

                          userId INT NOT NULL,
                          groupId INT NULL,

                          CONSTRAINT fk_password_user
                              FOREIGN KEY (userId)
                                  REFERENCES `User`(userId)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_password_group
                              FOREIGN KEY (groupId)
                                  REFERENCES `Group`(groupId)
                                  ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE PasswordTag (
                             tagId INT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO PasswordTag (name) VALUES
                                   ('WEBSITE'),
                                   ('APPLICATION'),
                                   ('SERVER'),
                                   ('DATABASE'),
                                   ('EMAIL');

ALTER TABLE Password
    DROP COLUMN tag,
    ADD COLUMN tagId INT NOT NULL,
    ADD CONSTRAINT fk_password_tag
        FOREIGN KEY (tagId)
            REFERENCES PasswordTag(tagId);

ALTER TABLE Password
    DROP COLUMN securityTag,
    ADD COLUMN securityTag ENUM(
        'LOW',
        'MIDDLE',
        'HIGH'
        ) NOT NULL;

ALTER TABLE Password
    DROP COLUMN tagId,
    ADD COLUMN tag ENUM(
        'WEBSITE',
        'APPLICATION',
        'SERVER',
        'DATABASE',
        'EMAIL'
        ) NOT NULL;
