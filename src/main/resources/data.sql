
-- -----------------------------------------------------
-- Clean generic data for spring_rest_api_starter
-- -----------------------------------------------------

DELETE FROM oauth_client_details;
DELETE FROM oauth_client_token;
DELETE FROM oauth_access_token;
DELETE FROM oauth_refresh_token;
DELETE FROM oauth_code;
DELETE FROM oauth_approvals;
DELETE FROM ClientDetails;

COMMIT;

-- -----------------------------------------------------
-- Data for table oauth_client_details
-- -----------------------------------------------------

INSERT INTO oauth_client_details
    (client_id, client_secret, scope, 
    authorized_grant_types,
    web_server_redirect_uri, authorities, 
    access_token_validity, refresh_token_validity,
    additional_information, autoapprove)
VALUES
    ('clientMobileIdPassword', 'secret', 'api,read,write',
    'password,authorization_code,refresh_token', 
    null, null, 
    36000, 36000, 
    null, true);

INSERT INTO oauth_client_details
    (client_id, client_secret, scope, 
    authorized_grant_types,
    web_server_redirect_uri, authorities, 
    access_token_validity, refresh_token_validity,
    additional_information, autoapprove)
VALUES
    ('clientWebIdPassword', 'secret', 'api,read,write',
    'password,authorization_code,refresh_token', 
    null, null, 
    36000, 36000, 
    null, true);

COMMIT;

