# Exercise 1

## Reflection – Clean Code and Secure Coding Practices

Pada proyek ini, saya telah mengimplementasikan dua fitur baru menggunakan Spring Boot, yaitu fitur Edit Product dan Delete Product. Setelah mengimplementasikan fitur tersebut, saya melakukan evaluasi terhadap source code berdasarkan prinsip clean code dan secure coding yang telah dipelajari pada modul ini.

---

## CLEAN CODE PRINCIPLES

### Separation of Concerns
Aplikasi ini menggunakan arsitektur berlapis yang jelas. Controller hanya menangani request dan response, Service berisi logika bisnis, Repository bertanggung jawab pada pengelolaan data, dan Model merepresentasikan entitas Product. Pemisahan ini membuat kode lebih mudah dibaca, dipahami, dan dirawat.

### Penamaan Class dan Method yang Jelas
Nama class seperti ProductController, ProductService, dan ProductRepository sudah menggambarkan tanggung jawab masing-masing. Method seperti create, findAll, findById, update, dan deleteById juga jelas menunjukkan fungsinya.

### Struktur Kode yang Konsisten
Setiap fitur (create, edit, delete) mengikuti alur yang konsisten dari Controller ke Service lalu ke Repository. Hal ini memudahkan pengembangan fitur baru dan debugging.

### Penghindaran Duplikasi Kode
Logika bisnis tidak ditulis langsung di Controller, tetapi didelegasikan ke Service. Hal ini menghindari duplikasi kode dan membuat Controller tetap sederhana.

---

## SECURE CODING PRACTICES

### Penggunaan HTTP Method yang Tepat
Fitur delete menggunakan metode POST, bukan GET, sehingga mengurangi risiko penghapusan data secara tidak sengaja melalui URL.

### Pembatasan Akses Data Melalui Service Layer
Controller tidak berinteraksi langsung dengan Repository. Semua akses data dilakukan melalui Service, sehingga logika bisnis dan validasi dapat dikontrol dengan lebih baik.

### Validasi Alur Data
Pada fitur edit, data produk diambil berdasarkan productId. Jika data tidak ditemukan, aplikasi melakukan redirect ke halaman list sehingga menghindari error seperti NullPointerException.

### Penggunaan Redirect Setelah Operasi Data
Setelah operasi create, update, dan delete, aplikasi melakukan redirect ke halaman list. Hal ini mencegah duplicate submission ketika halaman di-refresh.

---

## EVALUASI DAN PERBAIKAN KODE

### Penyimpanan Data Masih In-Memory
Saat ini data produk disimpan menggunakan List di Repository. Hal ini tidak aman untuk aplikasi produksi karena data akan hilang saat aplikasi dimatikan. Perbaikan yang dapat dilakukan adalah menggunakan database dan Spring Data JPA.

### Belum Ada Validasi Input
Aplikasi belum menerapkan validasi input seperti memastikan productName tidak kosong dan productQuantity tidak bernilai negatif. Perbaikan dapat dilakukan dengan menambahkan anotasi validasi seperti @NotBlank dan @Min.

### Tidak Ada Error Handling Khusus
Saat productId tidak ditemukan, aplikasi hanya melakukan redirect tanpa memberikan pesan kesalahan. Perbaikan dapat dilakukan dengan menambahkan handling error atau pesan feedback ke pengguna.

### Keamanan Masih Terbatas
Aplikasi belum menggunakan autentikasi dan otorisasi. Untuk meningkatkan keamanan, dapat ditambahkan Spring Security agar hanya pengguna tertentu yang dapat melakukan edit dan delete.

---

## KESIMPULAN

Secara keseluruhan, kode yang dibuat sudah menerapkan prinsip clean code dengan struktur yang jelas dan alur yang konsisten. Namun, masih terdapat beberapa aspek yang dapat ditingkatkan terutama pada validasi, error handling, dan keamanan agar aplikasi lebih siap digunakan pada lingkungan produksi.

---

# Exercise 2

## Refleksi setelah menulis Unit Test

Setelah menulis unit test, saya merasa lebih yakin terhadap perilaku kode yang saya buat karena setiap fungsi diuji secara terisolasi. Unit test membantu saya memahami edge case dan memastikan perubahan kode tidak merusak fitur yang sudah ada. Jumlah unit test dalam satu kelas tidak memiliki angka pasti, tetapi idealnya setiap method publik dan setiap kemungkinan kondisi penting (kasus normal, batas, dan error) memiliki test masing-masing. Untuk memastikan unit test sudah cukup, kita dapat menggunakan metrik seperti code coverage untuk melihat seberapa besar bagian kode yang dieksekusi oleh test. Namun, memiliki 100% code coverage tidak berarti kode bebas dari bug atau error, karena test bisa saja tidak memverifikasi logika dengan benar, tidak menguji skenario nyata, atau melewatkan bug yang bersifat konseptual dan integrasi antar komponen.

---

## Refleksi kebersihan kode pada Functional Test tambahan

Jika saya membuat functional test suite baru untuk memverifikasi jumlah item dalam product list dengan struktur dan setup yang sama seperti functional test sebelumnya, maka dari sisi kebersihan kode akan muncul beberapa masalah. Salah satu isu utama adalah duplikasi kode, seperti setup WebDriver, baseUrl, dan konfigurasi anotasi yang sama di banyak kelas test. Duplikasi ini menurunkan maintainability karena jika ada perubahan konfigurasi, maka semua kelas test harus diubah satu per satu. Selain itu, test menjadi lebih sulit dibaca karena banyak boilerplate code yang tidak langsung berkaitan dengan tujuan pengujian. Untuk memperbaikinya, setup umum dapat diekstrak ke superclass abstrak atau utility class khusus untuk functional test. Dengan begitu, setiap test suite hanya fokus pada skenario pengujian masing-masing, kode menjadi lebih bersih, lebih mudah dirawat, dan kualitas keseluruhan test code meningkat.

---

## Kesimpulan

Unit test dan functional test memiliki peran yang berbeda namun saling melengkapi. Unit test membantu memastikan logika internal berjalan dengan benar, sedangkan functional test memastikan sistem bekerja sesuai perspektif pengguna. Penerapan clean code dalam test code sama pentingnya dengan production code agar proyek tetap terstruktur dan maintainable dalam jangka panjang.
