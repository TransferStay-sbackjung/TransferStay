import React, { useEffect, useState } from 'react';
import NavBar from '../NavBar'; // 네비게이션 바 컴포넌트 임포트
import '../../styles/post/AllPost.css';
import { useNavigate } from "react-router-dom";
import {API_BASE_URL} from "../../App";

const AllPosts = () => {
    const [posts, setPosts] = useState([]); // 게시글 리스트를 저장할 상태
    const [loading, setLoading] = useState(true); // 로딩 상태 관리
    const [error, setError] = useState(null); // 오류 관리
    const navigate = useNavigate(); // useNavigate 훅을 사용하여 페이지 이동 기능

    useEffect(() => {
        // 전체 게시글 데이터를 API로부터 가져오는 함수
        const fetchPosts = async () => {
            try {
                const response = await fetch(`${API_BASE_URL}/api/v1/assignment-posts`); // 적절한 API 엔드포인트로 변경
                const result = await response.json();
                setPosts(result.data.content); // API 응답에서 게시글 데이터 설정
                setLoading(false); // 데이터 로드 완료 후 로딩 중지
            } catch (err) {
                setError('게시글을 불러오는 중 오류가 발생했습니다.'); // 오류 처리
                setLoading(false); // 로딩 중지
            }
        };

        fetchPosts(); // 컴포넌트 마운트 시 API 호출
    }, []);

    if (loading) {
        return <div>로딩 중...</div>; // 로딩 중일 때 표시
    }

    if (error) {
        return <div>{error}</div>; // 오류 발생 시 메시지 표시
    }

    // 게시글 클릭 시 포스트 디테일 페이지로 이동
    const handlePostClick = (postId) => {
        navigate(`/posts/${postId}`); // 해당 포스트의 디테일 페이지로 이동
    };

    return (
        <div className="main-page">
            {/* 네비게이션 바 추가 */}
            <NavBar />
            <h1>전체 게시글 조회</h1>
            <div className="posts-list">
                {posts.length > 0 ? (
                    posts.map((post) => (
                        <div
                            key={post.id}
                            className="post-item"
                            onClick={() => handlePostClick(post.id)} // 게시글 클릭 시 디테일 페이지로 이동
                        >
                            <div className="post-image">Image</div> {/* 이미지 영역 추가 */}
                            <div className="post-info">
                                <h2>{post.title}</h2>
                                <p>{post.description}</p>
                                <p>작성일: {new Date(post.createdAt).toLocaleDateString()}</p>
                                <p>가격: {post.price.toLocaleString()} 원</p>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>게시글이 없습니다.</p>
                )}
            </div>
        </div>
    );
};

export default AllPosts;
