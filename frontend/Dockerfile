FROM nginx:1.25-alpine

# Xóa config mặc định (cách này được nếu bạn thay toàn bộ nginx.conf)
RUN rm -f /etc/nginx/conf.d/*

# Copy config đã sửa ở trên
COPY nginx.conf /etc/nginx/nginx.conf

# Copy static files
COPY static /static

# Kiểm tra config khi build (rất hữu ích)
RUN nginx -t

EXPOSE 8088

# Dùng CMD mặc định của image là được
# CMD ["nginx", "-g", "daemon off;"]