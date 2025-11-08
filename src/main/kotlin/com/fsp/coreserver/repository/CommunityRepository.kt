package com.fsp.coreserver.repository

import com.fsp.coreserver.domain.Community
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommunityRepository : JpaRepository<Community, Long>
