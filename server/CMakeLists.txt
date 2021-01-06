cmake_minimum_required(VERSION 3.17)
project(server)

set(CMAKE_CXX_STANDARD 14)
find_package(Threads)

add_executable(server
        server.cpp)
target_link_libraries( server ${CMAKE_THREAD_LIBS_INIT} )
