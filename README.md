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
