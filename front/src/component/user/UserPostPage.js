import React, { useEffect, useState } from 'react';
import NavBar from '../NavBar'; // 네비게이션 바 컴포넌트 임포트
import '../../styles/user/UserPostPage.css'; // 스타일 파일 임포트
import { useNavigate } from "react-router-dom";
import {API_BASE_URL} from "../../App";

const UserPostsPage = () => {
    const navigate = useNavigate();

    const [userPosts, setUserPosts] = useState([]); // 내가 올린 게시물 상태
    const [loading, setLoading] = useState(true); // 로딩 상태

    useEffect(() => {
        const fetchUserPosts = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                navigate('/login');
            } else {
                try {
                    const response = await fetch(`${API_BASE_URL}/api/v1/user/posts`, {
                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    });

                    if (response.ok) {
                        const data = await response.json();
                        setUserPosts(data.data); // 서버에서 받아온 게시물 목록 설정
                    } else {
                        console.error("Failed to fetch user posts");
                    }
                } catch (error) {
                    console.error("Error fetching user posts", error);
                } finally {
                    setLoading(false); // 데이터 로드 후 로딩 상태 종료
                }
            }
        };

        fetchUserPosts();
    }, [navigate]);

    if (loading) {
        return <p>로딩 중...</p>;
    }

    return (
        <div>
            <NavBar /> {/* 네비게이션 바 */}
            <div className="user-posts-page">
                <h2>내가 올린 글</h2>
                <div className="user-posts-list">
                    {userPosts.length > 0 ? (
                        userPosts.map((post) => (
                            <div className="user-post-item" key={post.id}>
                                <h3>{post.title}</h3>
                                <p>{post.description}</p>
                                <p>가격: {post.price.toLocaleString()} 원</p>
                                <p>상태: {post.status}</p>
                                <button onClick={() => navigate(`/posts/${post.id}`)}>상세보기</button>
                            </div>
                        ))
                    ) : (
                        <p>올린 게시글이 없습니다.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default UserPostsPage;
