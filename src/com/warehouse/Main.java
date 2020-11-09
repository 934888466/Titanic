/*
* /**
 * 业务分析
 *
 1.登录
 1.1安全校验 冻结校验 --
 1.2请选择您要进行的操作吧
 1.2.1 XXX功能，请按1

 2.功能
 2.1查看自己拥有权限的仓库
 2.2选择仓库
 2.3 提示，您要进行的操作，1-查询 2-存货 3-取货
  -- 校验，是否有权限
 2.3.1 查询：直接出列表
 2.3.2 存货：商品清单、想要存入的数量、是否确认存入，一经确认，讲提交无法修改 （您已经XXXXXXXXXXXXXX）
 2.3.3 取货：

 3.系统管理员功能
 3.1查看用户信息
 3.2修改、锁定、重置密码、用户信息
 3.3注册用户

 3.4修改权限
 --修改角儿权限、创建角色，并绑定权限
 --修改用户角色、
 */

package com.warehouse;

import com.warehouse.bean.*;
import com.warehouse.utils.JDBCUtils;
import jdk.nashorn.internal.ir.CallNode;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    static Staff loginStaff = null;
    static JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            while (loginStaff == null) {
                login();
                int a = role(loginStaff.getId());
                if (a == 3) {
                    System.out.println("请选择您要进行操作的系统：1-仓库管理员系统，2-系统管理员系统");
                    a = scanner.nextInt();
                }
                switch (a) {
                    case 1:
                        System.out.println("欢迎来到仓库管理员系统");
                        if (loginStaff == null) continue;
                        System.out.println("请选择您要进行的操作: 1.查看自己的仓库信息");
                        String b = scanner.next();
                        switch (b) {
                            case "1":
                                HashMap<String, Warehouse> warehouse = getWarehouse();
                                System.out.println("请输入您要操作的仓库id号码");
                                String wareID = scanner.next();
                                if (warehouse.get(wareID) == null) {
                                    System.out.println("您没有此仓库的权限");
                                } else {
                                    System.out.println("请选择您要进行的操作:1.查询 2.存货 3.取货");
                                    int c = scanner.nextInt();
                                    switch (c) {
                                        case 1:
                                            staffquery(loginStaff.getId(), wareID);
                                            break;
                                        case 2:
                                            addgood(loginStaff.getId(), wareID);
                                            break;
                                        case 3:
                                            pickup(loginStaff.getId(), wareID);
                                            break;
                                        default:
                                            System.out.println("您输入的数字有误，请重新输入");
                                            break;
                                    }
                                }
                                break;
                        }
                        break;
                    case 2:
                        System.out.println("欢迎来到系统管理员操作系统");
                        System.out.println("请寻找您要进行的操作:1.查看用户信息 2.修改用户信息 3.注册用户信息");
                        int d = scanner.nextInt();
                        switch (d) {
                            case 1:
                                lookuser();
                                break;
                            case 2:
                                revise();
                                break;
                            case 3:
                                increase();
                                break;
                            default:
                                System.out.println("您输入的数字有误，请重新输入");
                                break;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {

        }
    }//系统选择，仓库管理员系统和系统管理员系统

    public static void login() {
        String sql = "select * from staff where username = ? and password = ?";
        System.out.println("请输入您的用户名:");
        String username = scanner.next();
        System.out.println("请输入您的密码:");
        String password = scanner.next();
        List<Staff> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Staff>(Staff.class), username, password);//创建集合
        if (query.size() > 0 && query.get(0) != null) {
            loginStaff = query.get(0); //获得集合索引，从0位置开始
            System.out.println("欢迎" + query.get(0).getName() + "回来，祝您工作顺利");
        } else {
            System.out.println("您输入的用户名或密码错误，请检查后重新输入");
        }
    } //登录账号密码功能

    public static HashMap<String, Warehouse> getWarehouse() {
        String sql = "select * from warehouse_staff ws left join warehouse on ws.warehouse_id = warehouse.id where ws.staff_id = ?";
        Integer id = loginStaff.getId();
        List<Warehouse> query2 = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Warehouse>(Warehouse.class), id);
        System.out.println("您拥有的仓库信息为:");
        HashMap<String, Warehouse> sw = new HashMap<>();
        for (Warehouse warehouse : query2) {
            System.out.println(warehouse);
            sw.put(warehouse.getId().toString(), warehouse);
        }
        return sw;
    }//查询仓库信息

    public static int role(int staffId) {
        int r = 0;
        String sql = "select role_id from staff_role where staff_id = ?";
        List<StaffRole> query3 = jdbcTemplate.query(sql, new BeanPropertyRowMapper<StaffRole>(StaffRole.class), staffId);
        if (query3.size() == 0) {
            System.out.println("您没有任何角色，如有需要请联系系统管理员");
        } else if (query3.size() > 1) {
            System.out.println("您是双系统管理人员");
            r = 3;
        } else if (1 == query3.get(0).getRoleId()) {
            System.out.println("您是仓库管理人员");
            r = 1;
        } else {
            System.out.println("您是系统管理人员");
            r = 2;
        }
        return r;
    }//判断是仓库管理员还是系统管理员

    public static void staffquery(int staffId, String warehouseId) {
        String sql = "select assess_id from staff_assess where staff_id = ?";
        List<StaffAssess> query4 = jdbcTemplate.query(sql, new BeanPropertyRowMapper<StaffAssess>(StaffAssess.class), staffId);
        for (StaffAssess staffAssess : query4) {
            if (staffAssess.getAssessId() == 1) {
                System.out.println("您拥有此功能权限，系统将为您查询");
                String sql1 = "select * from good,warehouse_good wh where wh.good_id = good.id and wh.warehouse_id = ?";
                List<WarehouseGoodDTO> query5 = jdbcTemplate.query(sql1, new BeanPropertyRowMapper<WarehouseGoodDTO>(WarehouseGoodDTO.class), warehouseId);
                for (WarehouseGoodDTO good : query5) {
                    System.out.println(good);
                }
                System.out.println("您查询的信息已显示");
                return;
            }
        }
        System.out.println("您没有此功能");

    }//查询所属仓库商品信息

    public static void addgood(int staffId, String warehouseId) {
        String sql = "select assess_id from staff_assess where staff_id = ?";
        List<StaffAssess> query6 = jdbcTemplate.query(sql, new BeanPropertyRowMapper<StaffAssess>(StaffAssess.class), staffId);
        for (StaffAssess staffAssess : query6) {
            if (staffAssess.getAssessId() == 2) {
                System.out.println("您具有存货功能，接下来进入存货系统");
                String sql1 = "select * from good,warehouse_good wg where wg.good_id = good.id and wg.warehouse_id = ?";
                List<WarehouseGoodDTO> query7 = jdbcTemplate.query(sql1, new BeanPropertyRowMapper<WarehouseGoodDTO>(WarehouseGoodDTO.class), warehouseId);
                HashMap<String, WarehouseGoodDTO> sw = new HashMap<>();
                for (WarehouseGoodDTO wg : query7) {
                    System.out.println(wg);
                    sw.put(wg.getGoodId().toString(), wg);
                }
                System.out.println("您要添加的商品是否在仓库中存在，有请输入1，没有请输入0");
                int a = scanner.nextInt();
                if (a == 1) {
                    System.out.println("请选择您要添加的商品ID");
                    String b = scanner.next();

                    System.out.println(sw.get(b));

                    int c = sw.get(b).getNumber();
                    System.out.println("请添加商品的数量");
                    int count = scanner.nextInt();
                    count = count + c;
                    String sql2 = "update warehouse_good set number = ? where warehouse_good.id = ?";
                    jdbcTemplate.update(sql2, count, b);
                    System.out.println("您已添加完成");
                } else if (a == 0) {
                    String sql3 = "select good.* from good";
                    List<Good> query8 = jdbcTemplate.query(sql3, new BeanPropertyRowMapper<Good>(Good.class));
                    for (Good good : query8) {
                        System.out.println(good);
                    }
                    System.out.println("请输入商品的ID");
                    int d = scanner.nextInt();
                    String sql4 = "select parent_id from good where id = ?";
                    List<Good> query9 = jdbcTemplate.query(sql4, new BeanPropertyRowMapper<Good>(Good.class), d);


                    if (query9 != null && query9.size() > 0 && query9.get(0) != null && query9.get(0).getParentId() > 0) {
                        System.out.println("请输入商品的数量");
                        int n = scanner.nextInt();
                        String sql5 = "insert into warehouse_good(warehouse_id,good_id,number) value(?,?,?)";
                        jdbcTemplate.update(sql5, warehouseId, d, n);
                        System.out.println("添加成功");
                    } else {
                        System.out.println("您输入错误，请查验后重新输入");
                    }
                } else {
                    System.out.println("输入有误，请查验后重新输入");
                }
                return;
            }
        }
        System.out.println("您没有存货功能，请联系管理人员查验后重新操作");
    }//存货功能

    public static void pickup(int staffId, String warehouseId) {
        String sql = "select assess_id from staff_assess where staff_id = ?";
        List<StaffAssess> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<StaffAssess>(StaffAssess.class), staffId);
        for (StaffAssess sa : query) {
            if (sa.getAssessId() == 3) {
                System.out.println("您具有取货功能，接下来进入取货系统");
                String sql2 = "select good.*,wg.* from warehouse_good wg,good where wg.good_id = good.id and wg.warehouse_id = ?";
                List<WarehouseGoodDTO> query2 = jdbcTemplate.query(sql2, new BeanPropertyRowMapper<WarehouseGoodDTO>(WarehouseGoodDTO.class), warehouseId);
                HashMap<String, WarehouseGoodDTO> sw = new HashMap<>();
                for (WarehouseGoodDTO wg : query2) {
                    System.out.println(wg);
                    sw.put(wg.getGoodId().toString(), wg);
                }
                System.out.println("请输入您要取货商品的ID:");
                String a = scanner.next();
                System.out.println("请输入您要取货的数量:");
                int b = scanner.nextInt();
                String sql3 = "select number from warehouse_good where good_id = ?";
                List<WarehouseGood> query3 = jdbcTemplate.query(sql3, new BeanPropertyRowMapper<WarehouseGood>(WarehouseGood.class), a);
                if (query3 != null && query3.size() > 0 && query3.get(0) != null && query3.get(0).getNumber() > b) {
                    b = query3.get(0).getNumber() - b;
                    String sql4 = "update warehouse_good set number = ? where good_id = ?";
                    jdbcTemplate.update(sql4, b, a);
                    System.out.println("您已取货成功");
                } else if (query3 != null && query3.get(0).getNumber() != null && query3.get(0).getNumber() == b) {
                    String sql5 = "delete from warehouse_good where good_id =?";
                    jdbcTemplate.update(sql5, a);
                    System.out.println("您已取货成功");
                } else {
                    System.out.println("您所要取出货物的数量大于仓库里的数量，所以取货失败");
                }
                return;
            }
        }
        System.out.println("您没有取货功能，请联系管理人员查验后重新操作");
    }//取货功能

    public static void lookuser() {
        String sql = "select staff.* from staff";
        List<Staff> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Staff>(Staff.class));
        for (Staff staff : query) {
            System.out.println(staff);
        }
    }//查看用户的信息

    public static void revise() {
        System.out.println("请选择您要修改用户的状态：1.锁定账户 2.修改账户密码");
        int a = scanner.nextInt();
        switch (a) {
            case 1:
                String sql = "select * from staff";
                List<Staff> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Staff>(Staff.class));
                HashMap<String, Staff> ss = new HashMap<>();
                for (Staff staff : query) {
                    System.out.println(staff);
                    ss.put(staff.getId().toString(), staff);
                }
                String sql2 = "update staff set locke = ? where staff.id = ?";
                System.out.println("请输入您要修改的用户ID:");
                String b = scanner.next();
                System.out.println("输入1为锁定账户，输入0为取消锁定账户");
                int c = scanner.nextInt();
                if (c == 1 || c == 0) {
                    jdbcTemplate.update(sql2, b, c);
                    System.out.println("您已修改成功");
                } else {
                    System.out.println("您操作未成功，请检查信息后重新输入");
                }

            case 2:
                String sql3 = "select * from staff";
                List<Staff> query2 = jdbcTemplate.query(sql3, new BeanPropertyRowMapper<Staff>(Staff.class));
                HashMap<String, Staff> ss1 = new HashMap<>();
                for (Staff staff : query2) {
                    System.out.println(staff);
                    ss1.put(staff.getId().toString(), staff);
                }
                String sql4 = "update staff set password = ? where id =?";
                System.out.println("请输入您要修改用户的ID:");
                String s1 = scanner.next();
                System.out.println("请输入密码，字符长度不能超过16位");
                String s2 = scanner.next();
                if (s2.length() <= 16) {
                    jdbcTemplate.update(sql4, s2, s1);
                    System.out.println("您已修改成功");
                } else {
                    System.out.println("您输入的有误，请查验后重新输入");
                }
                break;
            default:
                System.out.println("您输入的有误，请查验后重新输入");
                break;
        }
    }//修改用户的信息

    public static void increase() {
        String sql = "insert into staff values(?,?,?,?,?,?)";
        System.out.println("请输入您要新添加用户的名字(字符长度小于16位):");
        String s1 = scanner.next();
        System.out.println("请输入您要新添加用户的账号(字符长度小于20位):");
        String s2 = scanner.next();
        System.out.println("请输入您要新添加用户的密码(字符长度小于16位):");
        String s3 = scanner.next();
        System.out.println("请输入您要新添加用户的账户状态(0-锁定，1-开锁):");
        int i1 = scanner.nextInt();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String sql2 = "select username from staff";
        List<Staff> query = jdbcTemplate.query(sql2, new BeanPropertyRowMapper<Staff>(Staff.class));
        for (Staff s : query) {
            if (s2.equals(s.getUsername())) {
                System.out.println("注册失败，账号存在请查询后重新输入");
                return;
            }
        }
        if (s1.length() > 16 || s2.length() > 20 || s3.length() > 16) {
            System.out.println("注册失败，输入字符长度大于规定长度");
        } else {
            jdbcTemplate.update(sql, 0, s1, s2, s3, i1, sdf.format(date));
            System.out.println("您已注册成功");
        }
    }//注册用户信息
}