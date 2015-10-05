CREATE TABLE sbm_authority (
  username VARCHAR(55),
  role     ENUM('ADMIN', 'USER'),
  FOREIGN KEY (username) REFERENCES SBM_USER (name),
  UNIQUE INDEX sbm_authority_index (username, role)
)