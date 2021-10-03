
-- -----------------------------------------------------
-- Table oauth_client_details
-- -----------------------------------------------------
--DROP TABLE oauth_client_details;

CREATE TABLE IF NOT EXISTS oauth_client_details (
  client_id VARCHAR(255) PRIMARY KEY,
  resource_ids VARCHAR(255),
  client_secret VARCHAR(255),
  scope VARCHAR(255),
  authorized_grant_types VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(255)
);

-- -----------------------------------------------------
-- Table oauth_client_token
-- -----------------------------------------------------
--DROP TABLE oauth_client_token;

CREATE TABLE IF NOT EXISTS oauth_client_token (
  token_id VARCHAR(255),
  token BYTEA,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255)
);

-- -----------------------------------------------------
-- Table oauth_access_token
-- -----------------------------------------------------
--DROP TABLE oauth_access_token;

CREATE TABLE IF NOT EXISTS oauth_access_token (
  token_id VARCHAR(255),
  token BYTEA,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255),
  authentication BYTEA,
  refresh_token VARCHAR(255)
);

-- -----------------------------------------------------
-- Table oauth_refresh_token
-- -----------------------------------------------------
--DROP TABLE oauth_refresh_token;

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
  token_id VARCHAR(255),
  token BYTEA,
  authentication BYTEA
);

-- -----------------------------------------------------
-- Table oauth_code
-- -----------------------------------------------------
--DROP TABLE oauth_code;

CREATE TABLE IF NOT EXISTS oauth_code (
  code VARCHAR(255),
  authentication BYTEA
);

-- -----------------------------------------------------
-- Table oauth_approvals
-- -----------------------------------------------------
--DROP TABLE oauth_approvals;

CREATE TABLE IF NOT EXISTS oauth_approvals (
    userId VARCHAR(255),
    clientId VARCHAR(255),
    scope VARCHAR(255),
    status VARCHAR(10),
    expiresAt TIMESTAMP,
    lastModifiedAt TIMESTAMP
);

-- -----------------------------------------------------
-- Table ClientDetails
-- -----------------------------------------------------
--DROP TABLE ClientDetails;

CREATE TABLE IF NOT EXISTS ClientDetails (
  appId VARCHAR(255) PRIMARY KEY,
  resourceIds VARCHAR(255),
  appSecret VARCHAR(255),
  scope VARCHAR(255),
  grantTypes VARCHAR(255),
  redirectUrl VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation VARCHAR(4096),
  autoApproveScopes VARCHAR(255)
);
