package mainoop;

import java.util.InputMismatchException;
import java.util.Scanner;
import mainoop.product.Product;
import mainoop.product.ProductList;
import mainoop.shoppingcart.ShoppingCartList;
import mainoop.user.Admin;
import mainoop.user.AdminList;
import mainoop.user.Customer;
import mainoop.user.CustomerList;

//mỗi lần thêm sản phẩm, xóa sản phẩm, sửa sản phẩm sẽ có cùng method viết lại product.txt
//vào sẽ tự động có sẵn danh sách sản phẩm, bỏ thao tác 1
//làm class user. 2 class kế thừa user là: Customer, Admin 
//method user + verifyLogin() : bool
//Customer + register()



public class running {
    public static void main(String[] args) {
        ProductList productList = new ProductList("src/mainoop/data/product.txt");
        CustomerList customerList = new CustomerList("src/mainoop/data/Customer.txt");
        AdminList adminList = new AdminList("src/mainoop/data/Admin.txt");
        ShoppingCartList sh = new ShoppingCartList("");
        Scanner sc = new Scanner(System.in);
        boolean check = true;

        while(check) {
            System.out.println("=======================MENU=======================");
            System.out.println("1. Khách hàng");
            System.out.println("2. Quản lý");
            System.out.println("3. Thoát");
            System.out.println("==============================================");
            System.out.print("Nhập thao tác: ");

            int temp = 0;
            boolean checkInPut = false;
            while(!checkInPut) {
                try {
                    temp = sc.nextInt();
                    sc.nextLine();

                    if (temp >= 1 && temp <= 3) {
                        checkInPut = true;
                    } else {
                        System.out.print("Thao tác không đúng. Nhập lại: ");
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Thao tác không đúng. Nhập lại: ");
                    sc.nextLine();
                }
            }

            switch(temp) {
                case 1 ->  {    // 1. Khách hàng
                    Customer currentCustomer = new Customer();
                    boolean check1 = true;
                    boolean loginCheck = false;
                    while(check1) {
                        System.out.println("==================MENU KHÁCH HÀNG=======================");
                        if(!loginCheck) {
                            System.out.println("0. Quay lại");
                            System.out.println("1. Đăng nhập");
                            System.out.println("2. Đăng ký");

                        } else {
                            System.out.println("3. Xem chi tiết tài khoản");
                            System.out.println("4. Xem Danh sách sản phẩm");
                            System.out.println("5. Tìm sản phẩm");
                            System.out.println("6. Xem giỏ Hàng");
                            System.out.println("7. Thêm sản phẩm vào giỏ hàng");
                            System.out.println("8. Xóa sản phẩm khỏi giỏ hàng");
                            System.out.println("9. Đăng xuất");
                        }
                        System.out.println("==============================================");
                        
                        System.out.print("Nhập thao tác: ");
                        int thaoTac1 = sc.nextInt();
                        sc.nextLine();

                        if(loginCheck) {
                            while((thaoTac1 < 3) || (thaoTac1 > 9)) {
                                System.out.print("Thao tác không đúng.\nNhập thao tác: ");
                                thaoTac1 = sc.nextInt();
                                sc.nextLine();
                            }
                        } else {
                            while((thaoTac1 < 0) || (thaoTac1 > 2)) {
                                System.out.print("Thao tác không đúng.\nNhập thao tác: ");
                                thaoTac1 = sc.nextInt();
                                sc.nextLine();
                            }
                        }
                        
                        switch(thaoTac1) {
                            case 0 ->  {   // 0. Quay Lại
                                System.out.println("Đã quay lại");
                                check1 = false;
                            }
                            
                            case 1 ->  {   // 1. Đăng nhập
                                // nhập tên đăng nhập
                                System.out.print("Nhập tên tài khoản: ");
                                String name = sc.nextLine();

                                // Nhập mật khẩu
                                System.out.print("Nhập mật khẩu: ");
                                String password = sc.nextLine();

                                // kiểm tra đúng tài khoản không
                                currentCustomer = customerList.login(name, password);
                                
                                if(currentCustomer == null) {
                                    System.out.println("Sai thông tin đăng nhập");
                                } else {
                                    loginCheck = true;
                                    System.out.println("Đã đăng nhập");
                                }
                            }
                      
                            case 2 ->  {   // 2. Đăng ký
                                // nhập tên đăng nhập
                                System.out.print("Nhập tên tài khoản: ");
                                String name = sc.nextLine();

                                // nhập mật khẩu
                                System.out.print("Nhập mật khẩu: ");
                                String password = sc.nextLine();

                                // nhập địa chỉ
                                System.out.print("Nhập Địa chỉ: ");
                                String address = sc.nextLine();
                                
                                // tạo đối tượng khách hàng mới
                                currentCustomer = new Customer(customerList.getCustomerCount()+1,name , password, address);

                                // thêm khách hàng đó vào danh sách khách hàng
                                customerList.addCustomerToList(currentCustomer);
                                loginCheck = true;
                                System.out.println("Đăng ký thành công!");

                            }
                            
                            case 3 ->  {   // 3. Xem chi tiết tài khoản
                                System.out.println("Mã số: " + currentCustomer.getUserId());
                                System.out.println("Tên: " + currentCustomer.getCustomerName());
                                System.out.println("Mật khẩu: " + currentCustomer.getUserPassword());
                                System.out.println("Địa chỉ: " + currentCustomer.getCustomerAddress());
                            }

                            case 4 ->  {   // 4. Xem Danh sách sản phẩm
                                productList.viewProductsList();
                            }
                            
                            case 5 ->  {   // 5. Tìm sản phẩm
                                productList.searchProduct();
                            }
                            
                            case 6 ->  {   // 6. Xem giỏ hàng
                                currentCustomer.viewCartItems();
                            }

                            case 7 ->  {   // 7. Thêm sản phẩm vào giỏ hàng
                                productList.viewProductsList();
                                System.out.print("Nhập mã sản phẩm muốn mua: ");
                                int id = sc.nextInt();
                                sc.nextLine();
                                System.out.print("Nhập số lượng sản phẩm muốn mua: ");
                                int quantity = sc.nextInt();
                                sc.nextLine();
                                currentCustomer.addCartItems(productList.getProductById(id), quantity);
                                System.out.println("Thêm sản phẩm thành công!");

                                customerList.set(currentCustomer.getUserId() - 1,currentCustomer);
                            }

                            case 8 ->  {   // 8. Xóa sản phẩm khỏi giỏ hàng
                                currentCustomer.viewCartItems();
                                System.out.print("Nhập mã sản phẩm muốn xóa: ");
                                int id = sc.nextInt();
                                sc.nextLine();
                                System.out.print("Nhập số lượng sản phẩm muốn xóa: ");
                                int quantity = sc.nextInt();
                                sc.nextLine();
                                currentCustomer.removeCartItems(productList.getProductById(id), quantity);
                                customerList.set(currentCustomer.getUserId() - 1,currentCustomer);
                            }

                            case 9 ->  {   // 9. Đăng xuất
                                currentCustomer = null;
                                loginCheck = false;
                                System.out.println("Đã đăng Xuất");
                            }
                        }
                    }
                }

                case 2 ->  {    // 2. Quản lý
                    Admin admin;
                    boolean check2 = false;
                    System.out.print("Nhập tên tài khoản quản lý: ");
                    String name = sc.nextLine();
                    System.out.print("Nhập mật khẩu tài khoản quản lý: ");
                    String password = sc.nextLine();
                    
                    admin = adminList.login(name, password);
                    if(admin == null) {
                        System.out.println("sai thông tin đăng nhập");
                    } else {
                        System.out.println("đăng nhập thành công!");
                        check2 = true;
                    }

                    while(check2) {
                        System.out.println("=======================MENU=======================");
                        System.out.println("1. Xem danh sách sản phẩm.");
                        System.out.println("2. Thên sản phẩm.");
                        System.out.println("3. Xóa sản phẩm.");
                        System.out.println("6. Thoát.");
                        System.out.println("==============================================");
                        
                        System.out.print("Nhập thao tác: ");
                        int thaoTac2 = sc.nextInt();
                        sc.nextLine();

                        switch(thaoTac2) {
                            case 1 ->  {
                                productList.viewProductsList();
                            }
                            case 2 ->  {
                                System.out.print("nhập tên sản phẩm: ");
                                String nameProduct = sc.nextLine();
                                System.out.println("Nhập giá sản phẩm: ");
                                long priceProduct = sc.nextLong();
                                sc.nextLine();
                                System.out.println("Nhập số lượng sản phẩm: ");
                                int quantityProduct = sc.nextInt();
                                sc.nextLine();
                                productList.addProduct(nameProduct, priceProduct, quantityProduct);
                            }
                            case 3 ->  {
                                productList.viewProductsList();
                                System.out.print("Nhập mã sản phẩm cần xóa: ");
                                int id = sc.nextInt();
                                sc.nextLine();
                                Product product = productList.getProductById(id);
                                productList.RemoveProduct(product);
                            }
                            case 6 ->  {
                                check2 = false;
                                System.out.println("Đã thoát!");
                            }
                        }
                        // Xem danh sách sản phẩm
                        // Thêm sản phẩm
                        // xóa sản phẩm
                        // Thoát
                                            }
                }
                case 3 ->  {
                    check = false;
                    System.out.println("Đã thoát!");
                }
            }
            // 1. Khách hàng
            // 2. Quản lý
            // 3. Thoát
                    }

        sc.close();
    }
}
