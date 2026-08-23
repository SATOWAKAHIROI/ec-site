-- yamada@example.com のパスワードをBCryptハッシュ済みの値に更新する
UPDATE users
SET password = '$2a$10$mA4Q1B4XKe1h/lW1OGCvf.Alh.XUomqJj1hF.ss6qXYo3r61CS8sC'
WHERE email = 'yamada@example.com';
