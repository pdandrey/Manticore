-- version: 1
CREATE TABLE IF NOT EXISTS [Character] (
	_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	Name TEXT UNIQUE NOT NULL,
	Race TEXT NOT NULL,
	HeroicClass TEXT NOT NULL,
	ParagonClass TEXT NULL,
	EpicClass TEXT NULL,
	[Level] INTEGER NOT NULL,
	PortraitURL TEXT NULL,
	ImportedOn DATETIME NOT NULL DEFAULT (strftime('%Y%m%dT%H%M%S', 'now','localtime')),
	UpdatedOn DATETIME NULL
);