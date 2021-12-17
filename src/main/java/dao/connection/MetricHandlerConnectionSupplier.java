package dao.connection;

import monitoring.ExecutionMetrics;
import server.RequestContext;

import javax.sql.DataSource;
import java.sql.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class MetricHandlerConnectionSupplier implements ConnectionSupplier {

    private final DataSource dataSource;
    private final RequestContext context;

    public MetricHandlerConnectionSupplier(DataSource dataSource, RequestContext context) {
        this.dataSource = dataSource;
        this.context = context;
    }

    @Override
    public Connection get() {

        Connection connection;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ConnectionWrapper(connection) {

            @Override
            public Statement createStatement() throws SQLException {
                return new StatementWrapper(connection.createStatement()){
                    @Override
                    public int[] executeBatch() {
                        return measureExecution(this::checkedExecuteBatch);
                    }
                };
            }

            @Override
            public PreparedStatement prepareStatement(String sql) throws SQLException {
                return new PreparedStatementWrapper(connection.prepareStatement(sql)) {
                    @Override
                    public ResultSet executeQuery() {
                        return measureExecution(()-> {
                            try {
                                return preparedStatement.executeQuery();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    @Override
                    public int executeUpdate(){
                        return measureExecution(this::checkedExecuteUpdate);
                    }
                };
            }
        };
    }

    private <R> R measureExecution(Supplier<R> action){
        ExecutionMetrics metrics = context.getExecutionMetrics();

        long currentTime = System.nanoTime();

        R result = action.get();

        long executionTime = System.nanoTime() - currentTime;

        metrics.addDbRequestExecutionTime((int) TimeUnit.NANOSECONDS.toMillis(executionTime));
        metrics.incrementDbRequestNumber();

        return result;
    }
}
