CREATE TABLE users (
                       user_id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       is_active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
                       role_id BIGSERIAL PRIMARY KEY,
                       role_name VARCHAR(100) NOT NULL UNIQUE,
                       role_description VARCHAR(255)
);

CREATE TABLE permissions (
                             permission_id BIGSERIAL PRIMARY KEY,
                             permission_name VARCHAR(100) NOT NULL UNIQUE,
                             description VARCHAR(200)
);

CREATE TABLE screens (
                         screen_id BIGSERIAL PRIMARY KEY,
                         screen_name VARCHAR(100) NOT NULL,
                         screen_route VARCHAR(200) NOT NULL UNIQUE,
                         is_menu BOOLEAN DEFAULT TRUE,
                         parent_id BIGINT
);

CREATE TABLE user_roles (
                            user_role_id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
                            role_id BIGINT NOT NULL REFERENCES roles(role_id) ON DELETE CASCADE,
                            UNIQUE(user_id, role_id)
);

CREATE TABLE role_permissions (
                                  role_permission_id BIGSERIAL PRIMARY KEY,
                                  role_id BIGINT NOT NULL REFERENCES roles(role_id),
                                  permission_id BIGINT NOT NULL REFERENCES permissions(permission_id),
                                  UNIQUE(role_id, permission_id)
);

CREATE TABLE role_screen_permissions (
                                         role_screen_perm_id BIGSERIAL PRIMARY KEY,
                                         role_id BIGINT NOT NULL REFERENCES roles(role_id),
                                         screen_id BIGINT NOT NULL REFERENCES screens(screen_id),
                                         can_view BOOLEAN DEFAULT TRUE,
                                         can_edit BOOLEAN DEFAULT FALSE,
                                         can_delete BOOLEAN DEFAULT FALSE,
                                         UNIQUE(role_id, screen_id)
);

CREATE TABLE user_screen_permissions (
                                         usp_id BIGSERIAL PRIMARY KEY,
                                         user_id BIGINT NOT NULL REFERENCES users(user_id),
                                         role_id BIGINT REFERENCES roles(role_id),
                                         screen_id BIGINT REFERENCES screens(screen_id),
                                         can_view BOOLEAN DEFAULT TRUE,
                                         can_edit BOOLEAN DEFAULT FALSE,
                                         can_delete BOOLEAN DEFAULT FALSE,
                                         UNIQUE(user_id, screen_id)
);

CREATE TABLE accounts (
                          account_id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL REFERENCES users(user_id),
                          account_name VARCHAR(100),
                          account_type VARCHAR(50),
                          balance DECIMAL(12,2),
                          currency VARCHAR(10),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
                            category_id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT REFERENCES users(user_id),
                            category_name VARCHAR(100) NOT NULL,
                            type VARCHAR(20) NOT NULL
);

CREATE TABLE transactions (
                              transaction_id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL REFERENCES users(user_id),
                              account_id BIGINT NOT NULL REFERENCES accounts(account_id) ON DELETE CASCADE,
                              category_id BIGINT NOT NULL REFERENCES categories(category_id),
                              type VARCHAR(20),
                              amount DECIMAL(12,2),
                              description TEXT,
                              transaction_date TIMESTAMP,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE budgets (
                         budget_id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL REFERENCES users(user_id),
                         category_id BIGINT NOT NULL REFERENCES categories(category_id),
                         amount_limit DECIMAL(12,2),
                         start_date DATE,
                         end_date DATE
);

CREATE TABLE savings_goals (
                               goal_id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL REFERENCES users(user_id),
                               goal_name VARCHAR(100),
                               target_amount DECIMAL(12,2),
                               current_amount DECIMAL(12,2) DEFAULT 0,
                               deadline DATE
);

CREATE TABLE bill_reminders (
                                reminder_id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL REFERENCES users(user_id),
                                bill_name VARCHAR(100),
                                amount_due DECIMAL(12,2),
                                due_date DATE,
                                status VARCHAR(20) DEFAULT 'PENDING'
);

CREATE TABLE recurring_transactions (
                                        recurring_id BIGSERIAL PRIMARY KEY,
                                        user_id BIGINT NOT NULL REFERENCES users(user_id),
                                        account_id BIGINT NOT NULL REFERENCES accounts(account_id),
                                        category_id BIGINT NOT NULL REFERENCES categories(category_id),
                                        amount DECIMAL(12,2),
                                        frequency VARCHAR(20),
                                        next_run TIMESTAMP
);

CREATE TABLE tags (
                      tag_id BIGSERIAL PRIMARY KEY,
                      tag_name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE transaction_tags (
                                  id BIGSERIAL PRIMARY KEY,
                                  transaction_id BIGINT NOT NULL REFERENCES transactions(transaction_id) ON DELETE CASCADE,
                                  tag_id BIGINT NOT NULL REFERENCES tags(tag_id),
                                  UNIQUE(transaction_id, tag_id)
);
