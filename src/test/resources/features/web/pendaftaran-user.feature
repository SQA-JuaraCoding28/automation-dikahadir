@pendaftaranuser @management @web @test
Feature: Pendaftaran User
    As an authorized admin
    I want to register new users through the web admin panel
    So that I can add employees to the attendance system

    Background:
        Given user is on the web login page
        And user logs in with valid web credentials
        And web user should be redirected to the dashboard
        And admin navigates to Pendaftaran User page

    @positive @smoke
    Scenario: Successfully register a new user with complete valid data
        When admin uploads employee photo from "src/test/resources/test-data-image/image.jpeg"
        And admin enters NIK with "D8240008"
        And admin enters nama karyawan with "Kelompok1"
        And admin enters registration email with "kelompok1_test99@gmail.com"
        And admin enters registration password with "Kelompok1"
        And admin selects divisi with "!TestEditBerhasi09"
        And admin selects unit with "BCA"
        And admin selects posisi kerja with "Direktur"
        And admin selects jabatan with "Karyawan"
        And admin selects atasan with "Reza Kurni"
        And admin selects atasan client v3 with "Reid Reilly"
        And admin selects tipe kontrak with "Magang"
        And admin selects lokasi kerja with "DIKA Denpasar"
        And admin selects tipe shift with "Shift"
        And admin selects jadwal kerja with "BCA Syariah"
        And admin selects selfie with "No Selfie"
        And admin enters jumlah cuti with "20"
        And admin clicks the submit button
        Then admin should be redirected to the user management list page

    @negative
    Scenario: Register user with empty photo, NIK, and nama karyawan
        When admin enters registration email with "kelompok1_test99@gmail.com"
        And admin enters registration password with "Kelompok1"
        And admin selects divisi with "!TestEditBerhasi09"
        And admin selects unit with "BCA"
        And admin selects posisi kerja with "Direktur"
        And admin selects jabatan with "Karyawan"
        And admin selects atasan with "Reza Kurni"
        And admin selects atasan client v3 with "Reid Reilly"
        And admin selects tipe kontrak with "Magang"
        And admin selects lokasi kerja with "DIKA Denpasar"
        And admin selects tipe shift with "Shift"
        And admin selects jadwal kerja with "BCA Syariah"
        And admin selects selfie with "No Selfie"
        And admin enters jumlah cuti with "20"
        And admin clicks the submit button
        Then admin should remain on the pendaftaran user page

    @negative
    Scenario: Register user with empty email and password
        When admin uploads employee photo from "src/test/resources/test-data-image/image.jpeg"
        And admin enters NIK with "D8240008"
        And admin enters nama karyawan with "Kelompok1"
        And admin selects divisi with "!TestEditBerhasi09"
        And admin selects unit with "BCA"
        And admin selects posisi kerja with "Direktur"
        And admin selects jabatan with "Karyawan"
        And admin selects atasan with "Reza Kurni"
        And admin selects atasan client v3 with "Reid Reilly"
        And admin selects tipe kontrak with "Magang"
        And admin selects lokasi kerja with "DIKA Denpasar"
        And admin selects tipe shift with "Shift"
        And admin selects jadwal kerja with "BCA Syariah"
        And admin selects selfie with "No Selfie"
        And admin enters jumlah cuti with "20"
        And admin clicks the submit button
        Then admin should remain on the pendaftaran user page

    @negative
    Scenario: Register user with empty work information fields
        When admin uploads employee photo from "src/test/resources/test-data-image/image.jpeg"
        And admin enters NIK with "D8240008"
        And admin enters nama karyawan with "Kelompok1"
        And admin enters registration email with "kelompok1_test99@gmail.com"
        And admin enters registration password with "Kelompok1"
        And admin selects lokasi kerja with "DIKA Denpasar"
        And admin selects tipe shift with "Shift"
        And admin selects jadwal kerja with "BCA Syariah"
        And admin selects selfie with "No Selfie"
        And admin enters jumlah cuti with "20"
        And admin clicks the submit button
        Then admin should remain on the pendaftaran user page
