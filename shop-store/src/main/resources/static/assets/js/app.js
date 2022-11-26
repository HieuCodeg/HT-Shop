
class App {
    static DOMAIN_SERVER = "http://localhost:8080";
    static CUSTOMER_API = this.DOMAIN_SERVER + "/api/customers";
    static PROVINCE_API = "https://vapi.vnappmob.com/api/province/";
    static DISTRICT_API = this.PROVINCE_API + "district/";
    static WARD_API = this.PROVINCE_API + "ward/";

    static ROLE_API = this.DOMAIN_SERVER + "/api/auth/roles";
    static SIGNUP_API = this.DOMAIN_SERVER + "/api/auth/register";
    static SIGNIN_API = this.DOMAIN_SERVER + "/api/auth/login";

    static SERVER_CLOUDINARY = "https://res.cloudinary.com/dm86rpua0/image/upload";
    static SCALE_W250_H250_Q100 = "/c_limit,w_250,h_250,q_100";
    static SCALE_W150_H150_Q100 = "/c_limit,w_150,h_150,q_100";

    static PRODUCTS_API = this.DOMAIN_SERVER + "/api/products";
    static STAFF_API = this.DOMAIN_SERVER + "/api/staffs";

    static AlertMessageVi = class {
        static SUCCESS_CREATED = "Tạo dữ liệu thành công !";
        static SUCCESS_UPDATED = "Cập nhật dữ liệu thành công !";
        static SUCCESS_DEACTIVATE = "Hủy kích hoạt khách hàng thành công !";

        static ERROR_400 = "Thao tác không thành công, vui lòng kiểm tra lại dữ liệu.";
        static ERROR_401 = "Unauthorized - Access Token của bạn hết hạn hoặc không hợp lệ.";
        static ERROR_403 = "Forbidden - Bạn không được quyền truy cập tài nguyên này.";
        static ERROR_404 = "Not Found - Tài nguyên này đã bị xóa hoặc không tồn tại";
        static ERROR_500 = "Internal Server Error - Hệ thống Server đang có vấn đề hoặc không truy cập được.";

        static ERROR_ID_NOTFOUND = "Người dùng không tồn tại";

    }

    static SweetAlert = class {

        static showAlertSuccess(t) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: t,
                showConfirmButton: false,
                timer: 1500
            })
        }

        static showAlertError(t) {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: t,
                showConfirmButton: true
            })
        }

        static showSuspendedConfirmDialog() {
            return Swal.fire({
                icon: 'warning',
                text: 'Are you sure to suspend the selected customer ?',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, please suspend this client !',
                cancelButtonText: 'Cancel',
            })
        }
    }
}

class ProductAvatarDTO {
    constructor(fileName, fileFolder, fileUrl) {
        this.fileName = fileName;
        this.fileFolder = fileFolder;
        this.fileUrl = fileUrl;
    }
}

class Product {
    constructor(id, title, price,quantity, description, avatar) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.avatar = avatar;
    }
}

class Customer {
    constructor(id, fullName, email, phone, locationRegion, balance, deleted, avatarDTO) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.locationRegion = locationRegion;
        this.balance = balance;
        this.deleted = deleted;
        this.avatarDTO = avatarDTO;
    }
}
class Staff {
    constructor(id, fullName, email, phone, locationRegion, balance, deleted, avatarDTO) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.locationRegion = locationRegion;
        this.balance = balance;
        this.deleted = deleted;
        this.avatarDTO = avatarDTO;
    }
}
class Avatar {
    constructor(id, fileName, fileFolder, fileUrl, cloudId, fileType) {
        this.id = id;
        this.fileName = fileName;
        this.fileFolder = fileFolder;
        this.fileUrl = fileUrl;
        this.cloudId = cloudId;
        this.fileType = fileType;
    }
}
class LocationRegion {
    constructor(id, provinceId, provinceName, districtId, districtName, wardId, wardName, address) {
        this.id = id;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.address = address;
    }
}
class User{
    constructor(username,password,role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
class Role {
    constructor(id,code) {
        this.id = id;
        this.code = code;
    }
}


