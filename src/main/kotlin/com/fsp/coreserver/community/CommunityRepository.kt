package com.fsp.coreserver.community

import com.fsp.coreserver.community.Community
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommunityRepository : JpaRepository<Community, Long>