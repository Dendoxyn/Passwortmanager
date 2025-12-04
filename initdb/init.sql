CREATE TABLE `User` (
                        userId INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(100) NOT NULL UNIQUE,
                        hashedPwd VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE `Group` (
                         groupId INT AUTO_INCREMENT PRIMARY KEY,
                         groupName VARCHAR(100) NOT NULL,
                         creatorId INT NOT NULL,

                         CONSTRAINT fk_group_creator
                             FOREIGN KEY (creatorId)
                                 REFERENCES `User`(userId)
                                 ON DELETE CASCADE
) ENGINE=InnoDB;

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

CREATE TABLE Password (
                          passwordId INT AUTO_INCREMENT PRIMARY KEY,
                          website_app VARCHAR(255) NOT NULL,
                          loginName VARCHAR(255) NOT NULL,
                          encryptedPwd VARBINARY(255) NOT NULL,
                          note TEXT,

                          tag ENUM(
        'WEBSITE',
        'APPLICATION',
        'SERVER',
        'DATABASE',
        'EMAIL'
    ) NOT NULL,

                          securityTag ENUM(
        'LOW',
        'MIDDLE',
        'HIGH'
    ) NOT NULL,

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
