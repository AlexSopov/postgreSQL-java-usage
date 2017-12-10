CREATE TABLE gitHubUser (
    id INT PRIMARY KEY NOT NULL,
    name varchar(128)
);

CREATE TABLE IF NOT EXISTS gitHubRepository (
	id INT PRIMARY KEY NOT NULL,
    repositoryOwner INT NOT NULL REFERENCES gitHubUser(id),
    starsCount INT,
    description TEXT
);

CREATE TABLE IF NOT EXISTS gitHubCommit (
	sha varchar(64) PRIMARY KEY NOT NULL,
    repositoryId INT REFERENCES gitHubRepository(id) ON DELETE CASCADE,
    committerId INT REFERENCES gitHubUser(id),
    commitDate DATE NOT NULL,
    message TEXT
);

CREATE TABLE IF NOT EXISTS programmingLanguage (
    name varchar(32) PRIMARY KEY NOT NULL
);

CREATE TABLE IF NOT EXISTS repositoryProgrammingLanguage (
	repositoryId INT REFERENCES gitHubRepository(Id) ON DELETE CASCADE,
    programmingLanguage varchar(32) REFERENCES programmingLanguage(name) ON DELETE CASCADE,
	PRIMARY KEY(repositoryId, programmingLanguage)
);