package mainoop.Payment;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import mainoop.FilePaths;
import mainoop.ListInterface;
import mainoop.Running;
import mainoop.product.Product;
import mainoop.product.ProductList;
import mainoop.user.Customer;
import mainoop.user.CustomerList;

public class paymentlist implements ListInterface{
    private int maxDaDon = 0;  //mã đơn lớn nhất
    private ArrayList<payment> paymentList = new ArrayList<>(); //danh sách các đơn

    // hàm tạo không tham số
    public paymentlist() {}
    
    // hàm tạo có tham số
    public paymentlist(String path) {
        addFromFile(path);
    }

    //==================geter======================
    public int getMaxDaDon() {
        return this.maxDaDon;
    }
    public ArrayList<payment> getPaymentList() {
        return paymentList;
    }

    //==================seter======================
    public void setMaxDaDon(int maxDaDon) {
        this.maxDaDon = maxDaDon;
    }
    public void setPaymentList(ArrayList<payment> paymentList) {
        this.paymentList = paymentList;
    }

    @Override
    public void addFromFile(String path) {
        CustomerList customerList = Running.getCustomerList();  //lấy danh sách khách hàng
        ProductList productList = Running.getProductList(); //lấy danh sách sản phẩm

        // đọc file
        try {
            Scanner reader = new Scanner(new File(path));
            // đọc dòng đầu là mã đơn lớn nhất
            maxDaDon = Integer.parseInt(reader.nextLine());

            // khi còn dòng thì tiếp tục đọc
            while(reader.hasNextLine()) {
                // tạo 1 đơn thanh toán(bill) không có thông tin
                payment order = new payment();  

                reader.nextLine();
                String s = reader.nextLine();
                String[] sSplit = s.split("Ma don hang: ");
                // thêm mã đơn
                order.setId(Integer.parseInt(sSplit[1]));
                String[] s2 = reader.nextLine().split("Ten khach hang: ");
                // thêm khách hàng của đơn đó
                order.setCustomer(customerList.getCustomerByName(s2[1]));
                reader.nextLine();
                reader.nextLine();
                reader.nextLine();

                String s3 = reader.nextLine();
                // khi đọc hết các sản phẩm trong file
                while(!s3.equals("------------------------------------------------------")) {
                    // tách các thông tin của sản phẩm
                    String[] s3Split = s3.split("[ ]*[|][ ]*");
                    // thêm sản phẩm vào đơn
                    order.ThemDanhSachSanPham(productList.getProductByName(s3Split[0]), Integer.parseInt(s3Split[1]) );
                    s3 = reader.nextLine();
                }
                order.calcuaSumPriceProduct();
                reader.nextLine();
                // thêm tình trạng đơn
                order.setStatus(reader.nextLine());
                reader.nextLine();
                
                // thên vào danh sách đơn
                paymentList.add(order);

            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Scanner reader = new Scanner(new File(FilePaths.PAYEDBILL_PATH));
            // khi còn dòng thì tiếp tục đọc
            while(reader.hasNextLine()) {
                // tạo 1 đơn thanh toán(bill) không có thông tin
                payment order = new payment();  

                reader.nextLine();
                String s = reader.nextLine();
                String[] sSplit = s.split("Ma don hang: ");
                // thêm mã đơn
                order.setId(Integer.parseInt(sSplit[1]));
                String[] s2 = reader.nextLine().split("Ten khach hang: ");
                // thêm khách hàng của đơn đó
                order.setCustomer(customerList.getCustomerByName(s2[1]));
                reader.nextLine();
                reader.nextLine();
                reader.nextLine();

                String s3 = reader.nextLine();
                // khi đọc hết các sản phẩm trong file
                while(!s3.equals("------------------------------------------------------")) {
                    // tách các thông tin của sản phẩm
                    String[] s3Split = s3.split("[ ]*[|][ ]*");
                    // thêm sản phẩm vào đơn
                    order.ThemDanhSachSanPham(productList.getProductByName(s3Split[0]), Integer.parseInt(s3Split[1]) );
                    s3 = reader.nextLine();
                }
                order.calcuaSumPriceProduct();
                reader.nextLine();
                // thêm tình trạng đơn
                order.setStatus(reader.nextLine());
                reader.nextLine();
                
                // thên vào danh sách đơn
                paymentList.add(order);

            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToFile() {
        try {
            FileWriter writer = new FileWriter(FilePaths.BILL_PATH);
            // viết mã đơn lớn nhất vào dòng đầu của file
            writer.write(String.valueOf(maxDaDon));
            for(payment order : paymentList) {  // lặp qua tất cả các đơn hàng
                if(order.getStatus().equals("da giao hang thanh cong")) //nếu đơn đã giao thì không gi vào file bill.txt
                    continue;
                // viết đơn
                writer.write("\n========================= Hoa Don =====================\n");
                writer.write("Ma don hang: " + order.getId() + "\n");
                writer.write("Ten khach hang: " + order.getCustomer().getCustomerName() + "\n");
                writer.write("Dia chi: " + order.getCustomer().getCustomerAddress());
                writer.write("\n------------------------------------------------------\n");
                writer.write(String.format("%-20s|%-6s|%-14s|%s\n", "Ten hang", "SL", "Don gia", "Thanh tien"));
                // lặp qua các sản phẩm trong đơn
                for (Map.Entry<Product,Integer> en : order.getDanhSachSanPham().entrySet()) {
                    Product product = en.getKey();
                    Integer quantity = en.getValue();
                    // viết sản phẩm trong đơn
                    writer.write(String.format("%-20s|%-6s|%-14s|%s\n", product.getProductName(), quantity, product.getProductPrice(), product.getProductPrice() * quantity));
                }
                writer.write("------------------------------------------------------\n");
                writer.write("Tong cong: " + order.getSumPriceProduct());
                writer.write("\n"+ order.getStatus() +"\n");
                writer.write("======================================================");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // thêm đơn mới vào danh sách
    public void addBill(Customer customer) {
        maxDaDon++; //tăng mã đơn lớn nhất
        payment order = new payment(maxDaDon,customer, customer.getCartItem(), customer.getSumPriceProduct());
        paymentList.add(order); //thêm vào danh sách
        writeToFile();  //viết vào file

        ProductList productList = Running.getProductList(); //lấy danh sách sản phẩm
        // lặp qua các sản phẩm trong đơn
        for (Map.Entry<Product,Integer> en : order.getDanhSachSanPham().entrySet()) {
            Product product = en.getKey();
            Integer quantity = en.getValue();
            // giảm số sản phẩm trong kho
            product.setProductQuantity(product.getProductQuantity() - quantity);
            // ghi vào file
            productList.writeToFile();
        }
    }

    // lấy sản phẩm từ mã sản phẩm
    public payment getOrderById(int id) {
        for(payment order : paymentList) {
            if(order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    // xem đơn bất kỳ 
    public void viewOrder(payment order) {
        if(order == null) {
            return;
        }
        System.out.print("\n========================= Hoa Don =====================\n");
        System.out.print("Ma don hang: " + order.getId() + "\n");
        System.out.print("Ten khach hang: " + order.getCustomer().getCustomerName() + "\n");
        System.out.print("Dia chi: " + order.getCustomer().getCustomerAddress());
        System.out.print("\n------------------------------------------------------\n");
        System.out.print(String.format("%-20s|%-6s|%-14s|%s\n", "Ten hang", "SL", "Don gia", "Thanh tien"));
        // lặp qua các sản phẩm trong đơn
        for (Map.Entry<Product,Integer> en : order.getDanhSachSanPham().entrySet()) {
            Product product = en.getKey();
            Integer quantity = en.getValue();
            // in ra sản phẩm trong đơn
            System.out.print(String.format("%-20s|%-6s|%-14s|%s\n", product.getProductName(), quantity, product.getProductPrice(), product.getProductPrice() * quantity));
        }
        System.out.print("------------------------------------------------------\n");
        System.out.print("Tong cong: " + order.getSumPriceProduct());
        System.out.print("\n"+ order.getStatus() +"\n");
        System.out.print("======================================================\n");
    }

    // xem tình trạng TẤT CẢ các đơn hàng(ngoại trừ đơn đã giao)(ADMIN)
    public void viewOrderStatus() {
        if(paymentList.isEmpty())
            return;
        for(payment order : paymentList) {
            if(!(order.getStatus().equals("da giao hang thanh cong"))) {
                viewOrder(order);
            }
        }
    }

    // xem tình trạng các đơn của khách hàng(ngoại trừ đơn đã giao)(CUSTOMER)
    public void viewCustomerOrderStatus(Customer customer) {
        if(paymentList.isEmpty())
            return;
        boolean check = false; //kiểm tra có đơn đang giao không
        for(payment order : paymentList) {
            if(order.getCustomer() == customer && !(order.getStatus().equals("da giao hang thanh cong"))) {
                viewOrder(order);
            }
            if(order.getStatus().equals("don hang dang duoc giao") && order.getCustomer() == customer)
                check = true;
        }

        // nếu có đơn đang giao
        if(check) {
            Scanner sc = new Scanner(System.in);
            System.out.println("nhập (y) để xác nhận đã nhận được hàng");
            System.out.println("nhập (n) nếu chưa nhận");
            System.out.print("Thao tác: ");
            String checkTransit = sc.nextLine();
            if(checkTransit.toLowerCase().equals("y")) {    //xác nhận đã nhận hàng
                for(payment order : paymentList) {
                    if(order.getStatus().equals("don hang dang duoc giao") && order.getCustomer() == customer) {
                        order.setStatus("da giao hang thanh cong"); //đổi đơn thành đã giao
                        try {
                            FileWriter writer = new FileWriter(FilePaths.PAYEDBILL_PATH, true);
                            // viết đơn vào file
                            writer.write("========================= Hoa Don =====================\n");
                            writer.write("Ma don hang: " + order.getId() + "\n");
                            writer.write("Ten khach hang: " + order.getCustomer().getCustomerName() + "\n");
                            writer.write("Dia chi: " + order.getCustomer().getCustomerAddress());
                            writer.write("\n------------------------------------------------------\n");
                            writer.write(String.format("%-20s|%-6s|%-14s|%s\n", "Ten hang", "SL", "Don gia", "Thanh tien"));
                            // lặp qua các sản phẩm trong đơn
                            for (Map.Entry<Product,Integer> en : order.getDanhSachSanPham().entrySet()) {
                                Product product = en.getKey();
                                Integer quantity = en.getValue();
                                // viết sản phẩm trong đơn
                                writer.write(String.format("%-20s|%-6s|%-14s|%s\n", product.getProductName(), quantity, product.getProductPrice(), product.getProductPrice() * quantity));
                            }
                            writer.write("------------------------------------------------------\n");
                            writer.write("Tong cong: " + order.getSumPriceProduct());
                            writer.write("\n"+ order.getStatus() +"\n");
                            writer.write("======================================================\n");

                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // viết lại file bill.txt
                        writeToFile();
                    }
                }
            }
        }
    }

    // xem TẤT CẢ các đơn đã được giao(ADMIN)
    public void viewDeliveredOrders() {
        if(paymentList.isEmpty()) {
            return;
        }
        for(payment order : paymentList) {
            if(order.getStatus().equals("da giao hang thanh cong")) {
                viewOrder(order);
            }
        }
    }

    // xem các đơn đã được giao của khách hàng(CUSTOMER)
    public void viewCustomerDeliveredOrders(Customer customer) {
        if(paymentList.isEmpty()) {
            return;
        }
        for(payment order : paymentList) {
            if(order.getCustomer() == customer && order.getStatus().equals("da giao hang thanh cong")) {
                viewOrder(order);
            }
        }
    }

    // xác nhận đang giao đơn
    public void confirmedShipping(payment order) {
        if(order == null) {
            System.out.println("không thể thực hiện");
            return;
        }
        if(order.getStatus().equals("dang chuan bi don hang")) {
            order.setStatus("don hang dang duoc giao");
        }
        writeToFile();
    }

    // xác nhận tất cả các đơn đang được giao
    public void confirmedShippingAll() {
        for(payment order : paymentList) {
            if(order.getStatus().equals("dang chuan bi don hang")) {
                order.setStatus("don hang dang duoc giao");
            }
        }
        writeToFile();
    }
}
