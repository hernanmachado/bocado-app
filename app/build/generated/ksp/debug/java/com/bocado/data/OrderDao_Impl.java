package com.bocado.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.bocado.model.Order;
import com.bocado.model.OrderItem;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OrderDao_Impl implements OrderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Order> __insertionAdapterOfOrder;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Order> __deletionAdapterOfOrder;

  private final EntityDeletionOrUpdateAdapter<Order> __updateAdapterOfOrder;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOrderStatus;

  public OrderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOrder = new EntityInsertionAdapter<Order>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `orders` (`id`,`clientName`,`tableNumber`,`totalAmount`,`status`,`paymentMethod`,`items`,`created_at`,`qrCode`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Order entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getClientName());
        statement.bindLong(3, entity.getTableNumber());
        statement.bindDouble(4, entity.getTotalAmount());
        statement.bindString(5, entity.getStatus());
        if (entity.getPaymentMethod() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPaymentMethod());
        }
        final String _tmp = __converters.fromOrderItemList(entity.getItems());
        statement.bindString(7, _tmp);
        statement.bindString(8, entity.getCreatedAt());
        if (entity.getQrCode() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getQrCode());
        }
      }
    };
    this.__deletionAdapterOfOrder = new EntityDeletionOrUpdateAdapter<Order>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `orders` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Order entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfOrder = new EntityDeletionOrUpdateAdapter<Order>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `orders` SET `id` = ?,`clientName` = ?,`tableNumber` = ?,`totalAmount` = ?,`status` = ?,`paymentMethod` = ?,`items` = ?,`created_at` = ?,`qrCode` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Order entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getClientName());
        statement.bindLong(3, entity.getTableNumber());
        statement.bindDouble(4, entity.getTotalAmount());
        statement.bindString(5, entity.getStatus());
        if (entity.getPaymentMethod() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPaymentMethod());
        }
        final String _tmp = __converters.fromOrderItemList(entity.getItems());
        statement.bindString(7, _tmp);
        statement.bindString(8, entity.getCreatedAt());
        if (entity.getQrCode() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getQrCode());
        }
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateOrderStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE orders SET status = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrder(final Order order, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfOrder.insertAndReturnId(order);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOrder(final Order order, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfOrder.handle(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrder(final Order order, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfOrder.handle(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrderStatus(final int id, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOrderStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateOrderStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Order>> getAllOrders() {
    final String _sql = "SELECT * FROM orders ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"orders"}, new Callable<List<Order>>() {
      @Override
      @NonNull
      public List<Order> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientName = CursorUtil.getColumnIndexOrThrow(_cursor, "clientName");
          final int _cursorIndexOfTableNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tableNumber");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfItems = CursorUtil.getColumnIndexOrThrow(_cursor, "items");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfQrCode = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCode");
          final List<Order> _result = new ArrayList<Order>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Order _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpClientName;
            _tmpClientName = _cursor.getString(_cursorIndexOfClientName);
            final int _tmpTableNumber;
            _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final List<OrderItem> _tmpItems;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfItems);
            _tmpItems = __converters.toOrderItemList(_tmp);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpQrCode;
            if (_cursor.isNull(_cursorIndexOfQrCode)) {
              _tmpQrCode = null;
            } else {
              _tmpQrCode = _cursor.getString(_cursorIndexOfQrCode);
            }
            _item = new Order(_tmpId,_tmpClientName,_tmpTableNumber,_tmpTotalAmount,_tmpStatus,_tmpPaymentMethod,_tmpItems,_tmpCreatedAt,_tmpQrCode);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getOrderById(final int id, final Continuation<? super Order> $completion) {
    final String _sql = "SELECT * FROM orders WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Order>() {
      @Override
      @Nullable
      public Order call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientName = CursorUtil.getColumnIndexOrThrow(_cursor, "clientName");
          final int _cursorIndexOfTableNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tableNumber");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfItems = CursorUtil.getColumnIndexOrThrow(_cursor, "items");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfQrCode = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCode");
          final Order _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpClientName;
            _tmpClientName = _cursor.getString(_cursorIndexOfClientName);
            final int _tmpTableNumber;
            _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final List<OrderItem> _tmpItems;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfItems);
            _tmpItems = __converters.toOrderItemList(_tmp);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpQrCode;
            if (_cursor.isNull(_cursorIndexOfQrCode)) {
              _tmpQrCode = null;
            } else {
              _tmpQrCode = _cursor.getString(_cursorIndexOfQrCode);
            }
            _result = new Order(_tmpId,_tmpClientName,_tmpTableNumber,_tmpTotalAmount,_tmpStatus,_tmpPaymentMethod,_tmpItems,_tmpCreatedAt,_tmpQrCode);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Order>> getOrdersByStatus(final String status) {
    final String _sql = "SELECT * FROM orders WHERE status = ? ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, status);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"orders"}, new Callable<List<Order>>() {
      @Override
      @NonNull
      public List<Order> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientName = CursorUtil.getColumnIndexOrThrow(_cursor, "clientName");
          final int _cursorIndexOfTableNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tableNumber");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfItems = CursorUtil.getColumnIndexOrThrow(_cursor, "items");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfQrCode = CursorUtil.getColumnIndexOrThrow(_cursor, "qrCode");
          final List<Order> _result = new ArrayList<Order>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Order _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpClientName;
            _tmpClientName = _cursor.getString(_cursorIndexOfClientName);
            final int _tmpTableNumber;
            _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final List<OrderItem> _tmpItems;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfItems);
            _tmpItems = __converters.toOrderItemList(_tmp);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpQrCode;
            if (_cursor.isNull(_cursorIndexOfQrCode)) {
              _tmpQrCode = null;
            } else {
              _tmpQrCode = _cursor.getString(_cursorIndexOfQrCode);
            }
            _item = new Order(_tmpId,_tmpClientName,_tmpTableNumber,_tmpTotalAmount,_tmpStatus,_tmpPaymentMethod,_tmpItems,_tmpCreatedAt,_tmpQrCode);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
